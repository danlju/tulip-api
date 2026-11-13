package com.danlju.tulip.infrastructure.dataaccess.entity;
import jakarta.persistence.*;
import java.time.Instant;
@Entity
@Table(name = "build_outbox")
public class OutboxMessage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Long id;
    @Lob @Column(nullable = false, columnDefinition = "TEXT") public String payload;
    public Instant publishedAt;
    public OutboxMessage() {}
    public OutboxMessage(String payload) { this.payload = payload; }
}
