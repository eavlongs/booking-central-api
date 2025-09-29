package com.booking_central.api.services;

import com.booking_central.api.dto.AuthTokens;
import com.booking_central.api.dto.ExtractedOAuthUserData;
import com.booking_central.api.dto.JwtClaim;
import com.booking_central.api.dto.TokenBody;
import com.booking_central.api.enums.JwtType;
import com.booking_central.api.models.Provider;
import com.booking_central.api.models.User;
import com.booking_central.api.models.UserProvider;
import com.booking_central.api.repositories.ProviderRepository;
import com.booking_central.api.repositories.UserProviderRepository;
import com.booking_central.api.repositories.UserRepository;
import com.booking_central.api.security.JwtService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class AuthService {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserProviderRepository userProviderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProviderRepository providerRepository;
    // https://medium.com/@ebenezerb/understanding-the-self-invocation-problem-with-transactional-annotation-in-spring-2b5b06286880
    @Autowired
    @Lazy // https://www.baeldung.com/circular-dependencies-in-spring#2-use-lazy
    private AuthService self;
    private List<Provider> providers;

    @PostConstruct
    public void init() {
        this.providers = providerRepository.findAll();
    }

    public Optional<AuthTokens> requestToken(TokenBody body) {
        JwtClaim jwtClaim;
        try {
            jwtClaim = jwtService.extractJwtClaim(body.getToken());
        } catch (Exception e) {
            log.debug(e.getMessage());
            return Optional.empty();
        }

        if (jwtClaim.getType() != JwtType.AUTH_REQUEST) {
            return Optional.empty();
        }
        // we can check whether user exists, but we will ignore that, and use the token detail as source of truth
        // we can use the request token detail as source of truth, because the data is verified,
        // and the token is short-lived

        User user = new User(
                jwtClaim.getId(),
                jwtClaim.getFirstName(),
                jwtClaim.getLastName(),
                jwtClaim.getProfilePicture()
        );

        String accessToken = jwtService.buildToken(user, JwtType.ACCESS_TOKEN);
        String refreshToken = jwtService.buildToken(user, JwtType.REFRESH_TOKEN);

        AuthTokens authTokens = new AuthTokens(accessToken, refreshToken);
        return Optional.of(authTokens);
    }

    public Optional<AuthTokens> refreshToken(TokenBody body) {
        JwtClaim jwtClaim;
        try {
            jwtClaim = jwtService.extractJwtClaim(body.getToken());
        } catch (Exception e) {
            log.debug(e.getMessage());
            return Optional.empty();
        }

        if (jwtClaim.getType() != JwtType.REFRESH_TOKEN) {
            return Optional.empty();
        }

        Optional<User> user = userRepository.findById(jwtClaim.getId());

        if (user.isEmpty()) {
            return Optional.empty();
        }

        String accessToken = jwtService.buildToken(user.get(), JwtType.ACCESS_TOKEN);
        String refreshToken = jwtService.buildToken(user.get(), JwtType.REFRESH_TOKEN);

        AuthTokens authTokens = new AuthTokens(accessToken, refreshToken);
        return Optional.of(authTokens);
    }

    // currently can't handle account linking yet
    public User handleUserLogin(ExtractedOAuthUserData extractedOAuthUserData, Provider provider) {
        UserProvider userProvider = getUserProvider(
                extractedOAuthUserData.getProvider().getId(),
                extractedOAuthUserData.getId()
        );

        if (userProvider == null) {
            log.debug("Creating new user");
            return self.handleCreateUserTransaction(extractedOAuthUserData, provider);
        } else {
            log.debug("User exists, fetching user");
            return getAuthenticatedUser(extractedOAuthUserData, provider);
        }
    }

    public User getAuthenticatedUser(ExtractedOAuthUserData extractedOAuthUserData, Provider provider) {
        UserProvider userProvider = getUserProvider(
                provider.getId(),
                extractedOAuthUserData.getId()
        );

        if (userProvider == null) {
            throw new IllegalStateException("UserProvider not found");
        }

        return userRepository.findById(userProvider.getUserId()).orElseThrow(() -> new IllegalStateException("User " +
                "not found"));
    }

    @Transactional
    public User handleCreateUserTransaction(ExtractedOAuthUserData extractedOAuthUserData, Provider provider) {
        User user = createUser(extractedOAuthUserData);
        createUserProvider(user.getId(), provider.getId(), extractedOAuthUserData.getId());
        return user;
    }

    public User createUser(ExtractedOAuthUserData extractedOAuthUserData) {
        User newUser = new User();
        newUser.setFirstName(extractedOAuthUserData.getFirstName());
        newUser.setLastName(extractedOAuthUserData.getLastName());
        newUser.setProfilePicture(extractedOAuthUserData.getProfilePicture());

        userRepository.save(newUser);
        return newUser;
    }

    public UserProvider createUserProvider(UUID userId, int providerId, String providerGivenId) throws IllegalStateException {
        UserProvider existing = getUserProvider(providerId, providerGivenId);

        if (existing != null) {
            throw new IllegalStateException("User already exists");
        }

        UserProvider userProvider = new UserProvider();
        userProvider.setUserId(userId);
        userProvider.setProviderId(providerId);
        userProvider.setProviderGivenId(providerGivenId);

        userProviderRepository.save(userProvider);
        return userProvider;
    }

    public UserProvider getUserProvider(int providerId, String providerGivenId) {
        return userProviderRepository.findByProviderIdAndProviderGivenId(providerId, providerGivenId);
    }

    @Cacheable("auth_providers")
    public List<Provider> getProviders() {
        return this.providers;
    }
}
