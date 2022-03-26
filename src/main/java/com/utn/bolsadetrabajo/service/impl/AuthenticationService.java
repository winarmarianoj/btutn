package com.utn.bolsadetrabajo.service.impl;

import com.utn.bolsadetrabajo.mapper.UserMapper;
import com.utn.bolsadetrabajo.model.User;
import com.utn.bolsadetrabajo.model.enums.State;
import com.utn.bolsadetrabajo.repository.UserRepository;
import com.utn.bolsadetrabajo.security.authentication.AuthenticationRequest;
import com.utn.bolsadetrabajo.security.authentication.AuthenticationResponse;
import com.utn.bolsadetrabajo.security.utilSecurity.JwtUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements com.utn.bolsadetrabajo.service.interfaces.AuthenticationService {

    private AuthenticationManager authenticationManager;
    private JwtUtilService jwtTokenUtil;
    private UserDetailsServiceImpl userDetailsService;
    private MessageSource messageSource;
    private UserRepository userRepository;
    private UserMapper userMapper;

    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager, JwtUtilService jwtTokenUtil, UserDetailsServiceImpl userDetailsService, MessageSource messageSource, UserRepository userRepository, UserMapper userMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.messageSource = messageSource;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public AuthenticationResponse createJwt(AuthenticationRequest authenticationRequest) throws Exception {
        User user = userRepository.findByUsername(authenticationRequest.getUsername());
        try {
            if (user.getState().equals(State.ACTIVE)){
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                                authenticationRequest.getPassword()));
            }
        }
        catch (BadCredentialsException e) {
            throw new BadCredentialsException(messageSource.getMessage("authentication.create.jwt.failed",
                    new Object[] {e}, null));
        }
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        String jwt = jwtTokenUtil.generateToken(userDetails);
        return userMapper.responseLoginUserJason(user, jwt);
    }

}
