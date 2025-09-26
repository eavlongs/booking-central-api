package com.booking_central.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Entity
@Table(name = "user_providers")
@NoArgsConstructor
@AllArgsConstructor
public class UserProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String userId;
    private String providerId;
    private String providerGivenId;

    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;
}
