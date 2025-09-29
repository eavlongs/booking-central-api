package com.booking_central.api.repositories;

import com.booking_central.api.models.UserProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserProviderRepository extends JpaRepository<UserProvider, UUID> {
    UserProvider findByProviderIdAndProviderGivenId(int providerId, String providerGivenId);
}
