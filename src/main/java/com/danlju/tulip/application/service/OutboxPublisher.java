package com.danlju.tulip.application.service;
import com.danlju.tulip.infrastructure.dataaccess.OutboxRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import java.time.Instant;

@Service
public class OutboxPublisher {

    private final OutboxRepository repository;
    private final SqsClient sqs;
    private final String queueUrl;

    public OutboxPublisher(OutboxRepository repository, SqsClient sqs, @Value("${sqs.queue-url}") String queueUrl) {
        this.repository = repository; this.sqs = sqs; this.queueUrl = queueUrl;
    }

    @Transactional
    public void publish() {
        for (var message : repository.pending(PageRequest.of(0, 20))) {
            sqs.sendMessage(SendMessageRequest.builder().queueUrl(queueUrl).messageBody(message.payload).build());
            message.publishedAt = Instant.now();
            repository.save(message);
        }
    }
}
