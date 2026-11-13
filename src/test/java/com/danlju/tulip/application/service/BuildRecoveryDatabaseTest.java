package com.danlju.tulip.application.service;

import com.danlju.tulip.application.usecases.ProjectUseCases;
import com.danlju.tulip.core.domain.BuildStatus;
import com.danlju.tulip.infrastructure.dataaccess.*;
import com.danlju.tulip.scheduling.PublishBuildsTask;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/** Use a dedicated disposable MySQL database, never a development or production schema. */
@SpringBootTest
@EnabledIfEnvironmentVariable(named = "TULIP_TEST_DB_URL", matches = ".+")
class BuildRecoveryDatabaseTest {
    @DynamicPropertySource static void database(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> System.getenv("TULIP_TEST_DB_URL"));
        registry.add("spring.datasource.username", () -> System.getenv().getOrDefault("TULIP_TEST_DB_USER", "tulipdb"));
        registry.add("spring.datasource.password", () -> System.getenv().getOrDefault("TULIP_TEST_DB_PASSWORD", "password"));
    }
    @Autowired ProjectUseCases projects;
    @Autowired BuildService builds;
    @Autowired BuildCrudRepository buildRows;
    @Autowired BuildAttemptRepository attempts;
    @Autowired OutboxRepository outbox;
    @Autowired BuildOwnershipService ownership;
    @Autowired OutboxPublisher publisher;
    @Autowired PlatformTransactionManager transactions;
    @MockBean SqsClient sqs;
    @MockBean PublishBuildsTask scheduler;

    private UUID project() {
        var id = UUID.randomUUID();
        projects.createProject(id, "test-" + id, "https://example.test/" + id, "GENERIC");
        return id;
    }
    private Integer request(UUID project) {
        var result = builds.requestBuild(project.toString(), "main", "");
        return buildRows.findByPublicId(result.publicId()).getId();
    }
    @Test void twoProjectsCanBothHaveBuildOne() {
        var first = buildRows.findById(request(project())).orElseThrow();
        var second = buildRows.findById(request(project())).orElseThrow();
        assertEquals(1, first.getNumber());
        assertEquals(1, second.getNumber());
    }
    @Test void simultaneousDeliveriesHaveOneOwnerAndRestartFencesOldCallback() throws Exception {
        var id = request(project());
        var a = UUID.randomUUID().toString(); var b = UUID.randomUUID().toString();
        try (var threads = Executors.newFixedThreadPool(2)) {
            var gate = new CountDownLatch(1);
            var first = threads.submit(() -> { gate.await(); return ownership.claim(id, a); });
            var second = threads.submit(() -> { gate.await(); return ownership.claim(id, b); });
            gate.countDown();
            assertNotEquals(first.get(10, TimeUnit.SECONDS).granted(), second.get(10, TimeUnit.SECONDS).granted());
        }
        var old = attempts.findById(id).orElseThrow().token;
        new TransactionTemplate(transactions).executeWithoutResult(tx -> {
            var row = attempts.findById(id).orElseThrow();
            row.leaseUntil = Instant.now().minusSeconds(1); attempts.save(row);
        });
        var replacement = UUID.randomUUID().toString();
        assertTrue(ownership.claim(id, replacement).granted());
        assertThrows(org.springframework.web.server.ResponseStatusException.class,
                () -> ownership.update(id, old, BuildStatus.FAILED, "stale"));
        ownership.update(id, replacement, BuildStatus.COMPLETED, null);
        assertEquals(BuildStatus.COMPLETED, buildRows.findById(id).orElseThrow().getStatus());
        assertFalse(ownership.claim(id, UUID.randomUUID().toString()).granted());
    }
    @Test void committedOutboxSurvivesPublisherFailureAndReplacement() {
        request(project());
        when(sqs.sendMessage(any(SendMessageRequest.class))).thenThrow(new RuntimeException("offline"));
        assertThrows(RuntimeException.class, () -> publisher.publish());
        assertTrue(outbox.findAll().stream().anyMatch(row -> row.publishedAt == null));
        when(sqs.sendMessage(any(SendMessageRequest.class))).thenReturn(SendMessageResponse.builder().messageId("ok").build());
        var restarted = new OutboxPublisher(outbox, sqs, "queue");
        new TransactionTemplate(transactions).executeWithoutResult(tx -> restarted.publish());
        assertTrue(outbox.findAll().stream().noneMatch(row -> row.publishedAt == null));
    }
    @Test void rolledBackRequestDoesNotLeaveBuildOrMessage() {
        var project = project();
        long before = outbox.count();
        new TransactionTemplate(transactions).executeWithoutResult(tx -> {
            request(project); tx.setRollbackOnly();
        });
        assertTrue(buildRows.findByProjectPublicId(project).isEmpty());
        assertEquals(before, outbox.count());
    }
}
