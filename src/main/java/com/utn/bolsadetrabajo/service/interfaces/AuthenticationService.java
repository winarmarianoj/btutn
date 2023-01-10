package com.utn.bolsadetrabajo.service.interfaces;

import com.utn.bolsadetrabajo.security.authentication.AuthenticationRequest;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    ResponseEntity<?> createJwt(AuthenticationRequest authenticationRequest) throws Exception;
}
