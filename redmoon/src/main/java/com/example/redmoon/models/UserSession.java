package com.example.redmoon.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class UserSession {

    @ManyToOne
    private User user;

    private String Token;

}
