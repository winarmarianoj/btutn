package com.utn.bolsadetrabajo.service.impl;

import com.utn.bolsadetrabajo.mapper.UserMapper;
import com.utn.bolsadetrabajo.model.User;
import com.utn.bolsadetrabajo.model.enums.State;
import com.utn.bolsadetrabajo.repository.UserRepository;
import com.utn.bolsadetrabajo.security.authentication.AuthenticationRequest;
import com.utn.bolsadetrabajo.security.authentication.AuthenticationResponse;
import com.utn.bolsadetrabajo.security.utilSecurity.JwtUtilService;
import com.utn.bolsadetrabajo.util.Errors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements com.utn.bolsadetrabajo.service.interfaces.AuthenticationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtUtilService jwtTokenUtil;
    @Autowired private UserDetailsServiceImpl userDetailsService;
    @Autowired private MessageSource messageSource;
    @Autowired private UserRepository userRepository;
    @Autowired private UserMapper userMapper;
    @Autowired private Errors errors;

    @Override
    public ResponseEntity<?> createJwt(AuthenticationRequest authenticationRequest) throws Exception {
        User user;
        try {
            user = userRepository.findByUsernameByStateActive(authenticationRequest.getUsername());
            if (user.getState().equals(State.ACTIVE)){
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
            }
        }catch (BadCredentialsException e) {
            LOGGER.error("Incorrecto usuario y/o contraseña - {0}. Asegurese que su cuente este Activa." + e.getMessage());
            errors.logError("Incorrecto usuario y/o contraseña - {0}. Asegurese que su cuente este Activa." + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("authentication.create.jwt.failed", new Object[] {authenticationRequest.getUsername()}, null));
        }final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        String jwt = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.responseLoginUserJason(user, jwt));
    }

}
