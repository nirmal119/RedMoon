package com.example.redmoon.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private String token;

}
