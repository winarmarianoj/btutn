package com.utn.bolsadetrabajo.mapper;

import com.utn.bolsadetrabajo.dto.request.PersonDTO;
import com.utn.bolsadetrabajo.dto.response.ResponsePersonDto;
import com.utn.bolsadetrabajo.exception.PersonException;
import com.utn.bolsadetrabajo.model.*;
import com.utn.bolsadetrabajo.model.enums.Genre;
import com.utn.bolsadetrabajo.model.enums.Roles;
import com.utn.bolsadetrabajo.model.enums.TypeStudent;
import com.utn.bolsadetrabajo.repository.ParametersRepository;
import com.utn.bolsadetrabajo.repository.RoleRepository;
import com.utn.bolsadetrabajo.service.interfaces.UserService;
import com.utn.bolsadetrabajo.validation.AgeValidate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
public class ApplicantMapper {

    private final RoleRepository roleRepository;
    private final UserService userService;
    private final AgeValidate ageValidate;
    private final ParametersRepository parametersRepository;

    public ApplicantMapper(RoleRepository roleRepository, UserService userService, AgeValidate ageValidate, ParametersRepository parametersRepository) {
        this.roleRepository = roleRepository;
        this.userService = userService;
        this.ageValidate = ageValidate;
        this.parametersRepository = parametersRepository;
    }

    public Applicant toModel(Applicant app, PersonDTO dto) throws PersonException {
        int age = parametersRepository.findByValue("minAgeApplicant");
        if(!ageValidate.ageValidateApplicant(dto, age)) return null;

        Role role = roleRepository.getByRole(Roles.APPLICANT);
        User user = userService.saveUser(dto.getEmail(), dto.getPassword(), role);
        if(app == null) app = new Applicant();

        buildPerson(app, dto);
        app.setUser(user);
        return app;
    }

    public Applicant toUpdate(Applicant app, PersonDTO dto) {
        buildPerson(app, dto);
        app.setUser(userService.update(app, dto.getEmail(), dto.getPassword()));
        return app;
    }

    private void buildPerson(Applicant app, PersonDTO dto) {
        app.setOficialName(dto.getName());
        app.setLastName(dto.getSurname());
        app.setIdentification(dto.getIdentification());
        app.setPhoneNumber(dto.getPhoneNumber());
        app.setBirthDate(dto.getBirthDate());
        if(dto.getGenre().equals("FEMENINO")){
            app.setGenre(Genre.FEMALE);
        }else if (dto.getGenre().equals("MASCULINO")){
            app.setGenre(Genre.MALE);
        }else if (dto.getGenre().equals("OTRO")){
            app.setGenre(Genre.OTHER);
        }
        if(dto.getTypeStudent().equals("ACTIVO")){
            app.setTypeStudent(TypeStudent.ACTIVE);
        }else if (dto.getTypeStudent().equals("REGULAR")){
            app.setTypeStudent(TypeStudent.REGULAR);
        }else if (dto.getTypeStudent().equals("RECIBIDO")){
            app.setTypeStudent(TypeStudent.RECEIVED);
        }
        app.setDeleted(false);
    }

    public ResponsePersonDto toResponseApplicant(Applicant app, String message) {
        ResponsePersonDto dto = ResponsePersonDto.builder()
                .id(app.getId())
                .name(app.getOficialName())
                .surname(app.getLastName())
                .identification(app.getIdentification())
                .phoneNumber(app.getPhoneNumber())
                .email(app.getUser().getUsername())
                .role(app.getUser().getRole().getRole().toString())
                .genre(app.getGenre())
                .birthDate(app.getBirthDate())
                .typeStudent(app.getTypeStudent())
                .message(message)
                .uri(ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/person/{id}")
                        .buildAndExpand(app.getUser().getUserId()).toUri())
                .build();
        return dto;
    }

}
