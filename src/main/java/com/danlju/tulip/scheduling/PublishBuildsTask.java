package com.danlju.tulip.scheduling;
import com.danlju.tulip.application.service.OutboxPublisher;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;
import org.slf4j.LoggerFactory;
@Component
public class PublishBuildsTask {

    private final OutboxPublisher publisher;

    public PublishBuildsTask(OutboxPublisher publisher) {
        this.publisher = publisher;
    }

    @Scheduled(fixedDelayString = "${outbox.publish-delay-ms:1000}")
    public void publish() {
        try {
            publisher.publish();
        }
        catch (Exception e) {
            LoggerFactory.getLogger(getClass()).warn("Outbox publish failed; retrying next cycle", e);
        }
    }
}
