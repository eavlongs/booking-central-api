package com.booking_central.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "user_providers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserProvider extends BaseDateModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID userId;
    private int providerId;
    private String providerGivenId;
}
