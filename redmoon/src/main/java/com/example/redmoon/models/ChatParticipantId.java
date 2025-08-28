package com.example.redmoon.models;

import java.io.Serializable;
import java.util.Objects;

public class ChatParticipantId implements Serializable {
    private Long chat;
    private Long user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatParticipantId)) return false;
        ChatParticipantId that = (ChatParticipantId) o;
        return Objects.equals(chat, that.chat) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chat, user);
    }
}
