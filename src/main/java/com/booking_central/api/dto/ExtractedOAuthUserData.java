package com.booking_central.api.dto;

import com.booking_central.api.models.Provider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExtractedOAuthUserData {
    private String id;
    private String firstName;
    private String lastName;
    private String profilePicture;
    private Provider provider;

    public ExtractedOAuthUserData(DefaultOidcUser user, Provider provider) {
        this.provider = provider;

        this.id = user.getSubject();
        this.firstName = user.getGivenName();
        this.lastName = user.getFamilyName();
        this.profilePicture = user.getPicture();
    }
}
