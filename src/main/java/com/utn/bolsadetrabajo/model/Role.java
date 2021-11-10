package com.utn.bolsadetrabajo.model;

import com.utn.bolsadetrabajo.model.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "ROLE")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long roleId;

    @Enumerated(value = EnumType.STRING)
    @Column(unique = true)
    private Roles role;
}

