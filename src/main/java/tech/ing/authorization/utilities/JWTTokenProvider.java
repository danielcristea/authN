package tech.ing.authorization.utilities;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import tech.ing.authorization.model.CustomUserDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

@Component
@Slf4j
public class JWTTokenProvider {

    public static final String TOKEN_ISSUER = "ING Tech";
    private static final long EXPIRATION_TIME_IN_MILLISECONDS = 900_000;

    @Value("${jwt.secret}")
    private String secret;

    public String generateJwtToken(CustomUserDetails customUserDetails) {
        log.info("Generating JWT Token for user {}", customUserDetails.getUsername());
        final List<String> claims = getClaimsFromUser(customUserDetails);
        final Date issuedAt = new Date(System.currentTimeMillis());
        return JWT.create().withIssuer(TOKEN_ISSUER)
                .withIssuedAt(issuedAt)
                .withSubject(String.valueOf(customUserDetails.getUser().getId()))
                .withClaim("authorities", claims)
                .withExpiresAt(new Date(issuedAt.getTime() + EXPIRATION_TIME_IN_MILLISECONDS))
                .sign(HMAC512(secret.getBytes()));
    }

    public List<GrantedAuthority> getAuthorities(String token) {
        final List<String> claims = getClaimsFromToken(token);
        return claims.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public Authentication getAuthentication(String username, List<GrantedAuthority> authorities, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken userPasswordAuthToken = new
                UsernamePasswordAuthenticationToken(username, null, authorities);
        userPasswordAuthToken.setDetails(new WebAuthenticationDetailsSource().
                buildDetails(request));
        return userPasswordAuthToken;
    }

    public boolean isTokenValid(String username, String token) {
        final JWTVerifier verifier = getJWTVerifier();
        return StringUtils.isNotEmpty(username) &&
                !isTokenExpired(verifier, token);
    }

    public String getTokenSubject(String token) {
        final JWTVerifier verifier = getJWTVerifier();
        return verifier
                .verify(token)
                .getSubject();
    }

    private boolean isTokenExpired(JWTVerifier verifier, String token) {
        final Date expiration = verifier
                .verify(token)
                .getExpiresAt();
        return expiration.before(new Date());
    }

    private List<String> getClaimsFromToken(String token) {
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token)
                .getClaim("authorities")
                .asList(String.class);
    }

    private JWTVerifier getJWTVerifier() {
        JWTVerifier verifier;
        try {
            Algorithm algorithm = HMAC512(secret);
            verifier = JWT.require(algorithm)
                    .withIssuer(TOKEN_ISSUER)
                    .build();
        } catch (JWTVerificationException e) {
            log.error("JWT token verification error", e);
            throw new JWTVerificationException("Cannot verify token");
        }
        return verifier;
    }

    private List<String> getClaimsFromUser(CustomUserDetails customUserDetails) {
        return customUserDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }
}