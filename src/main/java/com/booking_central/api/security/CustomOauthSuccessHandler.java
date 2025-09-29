package com.booking_central.api.security;

import com.booking_central.api.config.AppConfig;
import com.booking_central.api.dto.ExtractedOAuthUserData;
import com.booking_central.api.enums.JwtType;
import com.booking_central.api.models.Provider;
import com.booking_central.api.models.User;
import com.booking_central.api.repositories.ProviderRepository;
import com.booking_central.api.services.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
@Slf4j
public class CustomOauthSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private ProviderRepository providerRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private JwtService jwtService;

    @Value(AppConfig.oauth2SuccessUrl)
    private String oauth2SuccessUrl;

    @Value(AppConfig.oauth2FailureUrl)
    private String oauth2FailureUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
        Optional<Provider> provider = getProvider(oidcUser.getIssuer());
        if (provider.isEmpty()) {
            response.sendRedirect(oauth2FailureUrl + "?error=Unknown+provider");
            return;
        }

        ExtractedOAuthUserData extractedOAuthUserData = new ExtractedOAuthUserData(
                oidcUser, provider.get()
        );

        try {
            User user = authService.handleUserLogin(extractedOAuthUserData, provider.get());
            String authToken = jwtService.buildToken(user, JwtType.AUTH_REQUEST);

            String redirectUrl = oauth2SuccessUrl + "?token=" + URLEncoder.encode(authToken, StandardCharsets.UTF_8);
            response.sendRedirect(redirectUrl);
        } catch (Exception e) {
            log.error("OAuth login failed: {}", e.getMessage());
            response.sendRedirect(URLEncoder.encode(oauth2FailureUrl + "?error=" + "Something went wrong, please try " +
                    "again", StandardCharsets.UTF_8));
        }

    }

    private Optional<Provider> getProvider(URL issuer) {
        for (Provider provider : authService.getProviders()) {
            System.out.println(provider.getIssuerUrl() + " vs " + issuer.toString());
            if (provider.getIssuerUrl().equalsIgnoreCase(issuer.toString())) {
                return Optional.of(provider);
            }
        }
        return Optional.empty();
    }
}
