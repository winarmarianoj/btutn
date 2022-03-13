package com.utn.bolsadetrabajo.mapper;

import com.utn.bolsadetrabajo.dto.request.PersonDTO;
import com.utn.bolsadetrabajo.dto.response.ResponsePersonDto;
import com.utn.bolsadetrabajo.exception.PersonException;
import com.utn.bolsadetrabajo.model.*;
import com.utn.bolsadetrabajo.model.enums.Roles;
import com.utn.bolsadetrabajo.repository.RoleRepository;
import com.utn.bolsadetrabajo.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
public class PersonMapper {

    private final RoleRepository roleRepository;
    private final UserService userService;

    public PersonMapper(RoleRepository roleRepository, UserService userService) {
        this.roleRepository = roleRepository;
        this.userService = userService;
    }

    public Person toModel(Person per, PersonDTO dto) throws PersonException {
        Role role = roleRepository.getByRole(Roles.UTN);
        User user = userService.saveUser(dto.getEmail(), dto.getPassword(), role);
        if(per == null) per = new Person();

        buildPerson(per, dto);
        per.setUser(user);
        return per;
    }

    public Person toUpdate(Person per, PersonDTO dto) {
        buildPerson(per, dto);
        per.setUser(userService.update(per, dto.getEmail(), dto.getPassword()));
        return per;
    }

    private void buildPerson(Person per, PersonDTO dto){
        per.setOficialName(dto.getName());
        per.setLastName(dto.getSurname());
        per.setIdentification(dto.getDni());
        per.setPhoneNumber(dto.getPhoneNumber());
        per.setDeleted(false);
    }

    public ResponsePersonDto toResponsePerson(Person person, String message) {
        ResponsePersonDto per = ResponsePersonDto.builder()
                .id(person.getId())
                .name(person.getOficialName())
                .surname(person.getLastName())
                .dni(person.getIdentification())
                .phoneNumber(person.getPhoneNumber())
                .email(person.getUser().getUsername())
                .message(message)
                .uri(ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/person/{id}")
                        .buildAndExpand(person.getUser().getUserId()).toUri())
                .build();
        return per;
    }

}
