package com.booking_central.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "providers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Provider extends BaseDateModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String logoUrl;
    private String issuerUrl;
    private String registrationId;
}
