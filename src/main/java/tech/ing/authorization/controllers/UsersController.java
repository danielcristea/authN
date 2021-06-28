package tech.ing.authorization.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tech.ing.authorization.model.CustomUserDetails;
import tech.ing.authorization.model.LoginRequestDto;
import tech.ing.authorization.services.AuthenticationProviderService;
import tech.ing.authorization.services.JpaUserDetailsService;
import tech.ing.authorization.utilities.JWTTokenProvider;

import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;

@RestController
public class UsersController {

    public static final String JWT_TOKEN_HEADER_NAME = "Jwt-Token";
    private final AuthenticationProviderService authenticationProviderService;
    private final JWTTokenProvider jwtTokenProvider;
    private final JpaUserDetailsService jpaUserDetailsService;

    @Autowired
    public UsersController(AuthenticationProviderService authenticationProviderService, JWTTokenProvider jwtTokenProvider, JpaUserDetailsService jpaUserDetailsService) {
        this.authenticationProviderService = authenticationProviderService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.jpaUserDetailsService = jpaUserDetailsService;
    }

    @PostMapping("/v1/user/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequestDto loginRequest) {
        authenticationProviderService.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        CustomUserDetails userDetails = jpaUserDetailsService.loadUserByUsername(loginRequest.getUsername());
        HttpHeaders jwtHeader = setJwtHeader(userDetails);
        return new ResponseEntity<>(jwtHeader, OK);
    }

    private HttpHeaders setJwtHeader(CustomUserDetails userDetails) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER_NAME, jwtTokenProvider.generateJwtToken(userDetails));
        return headers;
    }
}
