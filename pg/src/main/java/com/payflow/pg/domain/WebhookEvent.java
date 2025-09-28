package com.payflow.pg.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "webhook_events")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class WebhookEvent {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime receivedAt;

    @Column(length = 200)
    private String signature;

    @Column(length = 100)
    private String event;

    @Column(length = 100)
    private String tid;

    @Column(length = 50)
    private String status;

    @Column(columnDefinition = "TEXT")
    private String headers;

    @Column(columnDefinition = "TEXT")
    private String body;

    private Boolean signatureValid;
}
