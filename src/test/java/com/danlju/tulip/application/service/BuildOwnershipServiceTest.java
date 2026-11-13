package com.danlju.tulip.application.service;
import com.danlju.tulip.infrastructure.dataaccess.*;
import com.danlju.tulip.infrastructure.dataaccess.entity.*;
import com.danlju.tulip.core.domain.BuildStatus;
import org.junit.jupiter.api.*;
import org.springframework.web.server.ResponseStatusException;
import java.time.Instant;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BuildOwnershipServiceTest {
    final BuildCrudRepository builds = mock(BuildCrudRepository.class);
    final BuildAttemptRepository attempts = mock(BuildAttemptRepository.class);
    final BuildOwnershipService service = new BuildOwnershipService(builds, attempts);
    final String owner = UUID.randomUUID().toString();
    final String next = UUID.randomUUID().toString();
    BuildDbEntity entity;
    @BeforeEach void setup() {
        entity = new BuildDbEntity(1, UUID.randomUUID(), 1, ProjectDbEntity.load(1), BuildStatus.RUNNING,
                "url", "main", "sha", "message", Instant.now(), Instant.now());
        when(builds.lockById(1)).thenReturn(Optional.of(entity));
        when(attempts.findById(1)).thenReturn(Optional.of(new BuildAttempt(1, owner, Instant.now().plusSeconds(120))));
    }
    @Test void duplicateCannotClaimRunningBuild() {
        assertFalse(service.claim(1, next).granted());
        verify(attempts, never()).save(any());
    }
    @Test void retryWithSameTokenIsIdempotent() {
        assertTrue(service.claim(1, owner).granted());
    }
    @Test void expiredLeaseAllowsReplacementAfterWorkerRestart() {
        when(attempts.findById(1)).thenReturn(Optional.of(new BuildAttempt(1, owner, Instant.now().minusSeconds(1))));
        assertTrue(new BuildOwnershipService(builds, attempts).claim(1, next).granted());
    }
    @Test void staleCallbackCannotOverwriteNewAttempt() {
        assertThrows(ResponseStatusException.class, () -> service.update(1, next, BuildStatus.FAILED, "late"));
        verify(builds, never()).save(any());
    }
    @Test void expiredOwnerCannotComplete() {
        when(attempts.findById(1)).thenReturn(Optional.of(new BuildAttempt(1, owner, Instant.now().minusSeconds(1))));
        assertThrows(ResponseStatusException.class, () -> service.update(1, owner, BuildStatus.COMPLETED, null));
    }
    @Test void terminalDeliveryIsAcknowledgedWithoutExecution() {
        entity = new BuildDbEntity(1, UUID.randomUUID(), 1, ProjectDbEntity.load(1), BuildStatus.COMPLETED,
                "url", "main", "sha", "message", Instant.now(), Instant.now());
        when(builds.lockById(1)).thenReturn(Optional.of(entity));
        assertEquals(new BuildOwnershipService.Claim("COMPLETED", false), service.claim(1, next));
    }
    @Test void completionRetryFromOwnerIsIdempotent() {
        var completed = new BuildDbEntity(1, UUID.randomUUID(), 1, ProjectDbEntity.load(1), BuildStatus.COMPLETED,
                "url", "main", "sha", "message", Instant.now(), Instant.now());
        when(builds.lockById(1)).thenReturn(Optional.of(completed));
        assertEquals(BuildStatus.COMPLETED, service.update(1, owner, BuildStatus.COMPLETED, null));
        assertThrows(ResponseStatusException.class, () -> service.update(1, next, BuildStatus.COMPLETED, null));
    }
}
