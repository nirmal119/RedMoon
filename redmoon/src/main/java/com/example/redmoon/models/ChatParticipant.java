package com.example.redmoon.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;

@Data
@Entity
@Table(name = "chat_participants")
@IdClass(ChatParticipantId.class) // composite PK
public class ChatParticipant {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ParticipantRole role = ParticipantRole.MEMBER;

    @Column(name = "joined_at", nullable = false, updatable = false)
    private Instant joinedAt = Instant.now();

    @Column(name = "last_read_message_id")
    private Long lastReadMessageId;
}
