package com.utn.bolsadetrabajo.model;

import com.utn.bolsadetrabajo.model.enums.State;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "USER")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long userId;

    @NotNull(message = "El campo email es obligatorio")
    @Column
    @Email
    private String username;

    @NotNull(message = "El campo de su contraseña es obligatorio")
    @Column
    private String password;

    @Column
    private String verificationCode;

    @Enumerated(value = EnumType.STRING)
    @Column
    private State state;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "roleId")
    private Role role;

}

