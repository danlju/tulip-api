package com.danlju.tulip.application.service;
import com.danlju.tulip.infrastructure.dataaccess.OutboxRepository;
import com.danlju.tulip.infrastructure.dataaccess.entity.OutboxMessage;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
class OutboxPublisherTest {
    @Test void unsentMessageSurvivesPublisherReplacement() {
        var repository = mock(OutboxRepository.class);
        var sqs = mock(SqsClient.class);
        var message = new OutboxMessage("{\"build\":{\"id\":\"1\"}}");
        when(repository.pending(any())).thenReturn(List.of(message));
        when(sqs.sendMessage(any(SendMessageRequest.class))).thenThrow(new RuntimeException("offline"))
                .thenReturn(SendMessageResponse.builder().messageId("sent").build());
        assertThrows(RuntimeException.class, () -> new OutboxPublisher(repository, sqs, "queue").publish());
        assertNull(message.publishedAt);
        verify(repository, never()).save(any());
        new OutboxPublisher(repository, sqs, "queue").publish();
        assertNotNull(message.publishedAt);
        verify(repository).save(message);
    }
}
