package com.example.dodam.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.example.dodam.config.auth.AuthProperties;
import com.example.dodam.config.auth.AuthProperties.Auth;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TokenProviderTest {

    private static final String USERNAME = "tester";

    @Mock
    private Auth auth;

    private TokenProvider tokenProvider;
    private AuthProperties authProperties;
    private String secretKey;
    private String accessToken;


    @BeforeEach
    void init() {
        authProperties = new AuthProperties();
        authProperties.setAuth(auth);
        tokenProvider = new TokenProvider(authProperties);
        secretKey = UUID.randomUUID().toString();

        accessToken = Jwts.builder()
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .setSubject(USERNAME)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 2000L))
            .compact();
    }

    @Test
    void generateToken() {
        setMockProperties();

        String token = tokenProvider.generateToken(USERNAME);

        assertThat(token).isNotNull();
        System.out.println("token = " + token);
    }

    @Test
    void getAttribute() {
        setMockProperties();

        String attribute = tokenProvider.getAttribute(accessToken);

        assertThat(attribute).isEqualTo(USERNAME);
    }

    @Test
    void validateAccessToken() {
        setMockProperties();

        boolean result = tokenProvider.validateAccessToken(accessToken);

        assertThat(result).isTrue();
    }

    @Test
    void inputExpiredToken() throws InterruptedException {
        setMockProperties();

        Thread.sleep(3000L);
        boolean result = tokenProvider.validateAccessToken(accessToken);

        assertThat(result).isFalse();
    }

    @Test
    void inputMalformedToken() {
        setMockProperties();
        String malformedToken = "not token";

        boolean result = tokenProvider.validateAccessToken(malformedToken);

        assertThat(result).isFalse();
    }

    @Test
    void inputBlankToken() {
        setMockProperties();
        String blankToken = "";

        boolean result = tokenProvider.validateAccessToken(blankToken);

        assertThat(result).isFalse();
    }

    private void setMockProperties() {
        when(auth.getTokenSecret()).thenReturn(secretKey);
    }
}
