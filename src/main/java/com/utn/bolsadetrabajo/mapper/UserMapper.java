package com.utn.bolsadetrabajo.mapper;

import com.utn.bolsadetrabajo.dto.response.ResponseUserDto;
import com.utn.bolsadetrabajo.exception.PersonException;
import com.utn.bolsadetrabajo.model.Role;
import com.utn.bolsadetrabajo.model.User;
import com.utn.bolsadetrabajo.model.enums.State;
import com.utn.bolsadetrabajo.security.authentication.AuthenticationResponse;
import com.utn.bolsadetrabajo.util.GenerateHash;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final GenerateHash generateHash;

    public UserMapper(GenerateHash generateHash) {
        this.generateHash = generateHash;
    }

    public User toModel(String email, Role role, String encode) throws PersonException {
        Long pass = generateHash.generateAleatorio();
        User user = User.builder()
                .username(email)
                .password(encode)
                .role(role)
                .state(State.REVIEW)
                .verificationCode(String.valueOf(pass))
                .build();
        return user;
    }

    public ResponseUserDto toUserResponseDto(User user, String message) {
        ResponseUserDto us = ResponseUserDto.builder()
                .id(user.getUserId())
                .username(user.getUsername())
                .role(user.getRole().getRole().toString())
                .message(message)
                .build();
        return us;
    }

    public AuthenticationResponse responseLoginUserJason(User user, String jwt) {
        AuthenticationResponse auth = new AuthenticationResponse(jwt);
        auth.setUsername(user.getUsername());
        auth.setId(user.getUserId());
        auth.setRole(user.getRole());
        return auth;
    }

    public User update(User user, String email, String encodePassword) {
        user.setUsername(email);
        user.setPassword(encodePassword);
        return user;
    }
}
