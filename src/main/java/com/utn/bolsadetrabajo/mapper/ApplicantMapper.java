package com.utn.bolsadetrabajo.mapper;

import com.utn.bolsadetrabajo.dto.request.ApplicantDTO;
import com.utn.bolsadetrabajo.dto.response.ResponseApplicantDto;
import com.utn.bolsadetrabajo.dto.response.ResponseApplicantList;
import com.utn.bolsadetrabajo.exception.PersonException;
import com.utn.bolsadetrabajo.model.*;
import com.utn.bolsadetrabajo.model.enums.Genre;
import com.utn.bolsadetrabajo.model.enums.Roles;
import com.utn.bolsadetrabajo.model.enums.TypeStudent;
import com.utn.bolsadetrabajo.repository.ParametersRepository;
import com.utn.bolsadetrabajo.repository.RoleRepository;
import com.utn.bolsadetrabajo.service.UserService;
import com.utn.bolsadetrabajo.validation.AgeValidate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

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

    public Applicant toModel(Applicant app, ApplicantDTO dto) throws PersonException {
        int age = parametersRepository.findByValue("minAgeApplicant");
        if(!ageValidate.ageValidateApplicant(dto, age)) return null;

        Role role = roleRepository.getByRole(Roles.APPLICANT);
        User user = userService.saveUser(dto.getEmail(), dto.getPassword(), role);
        if(app == null) app = new Applicant();

        buildPerson(app, dto);
        app.setUser(user);
        return app;
    }

    public Applicant toUpdate(Applicant app, ApplicantDTO dto) {
        buildPerson(app, dto);
        app.setUser(userService.update(app, dto.getEmail(), dto.getPassword()));
        return app;
    }

    private void buildPerson(Applicant app, ApplicantDTO dto) {
        app.setOficialName(dto.getName());
        app.setLastName(dto.getSurname());
        app.setIdentification(dto.getDni());
        app.setPhoneNumber(dto.getPhoneNumber());
        app.setBirthDate(dto.getBirthDate());
        app.setGenre(Genre.valueOf(dto.getGenre()));
        app.setTypeStudent(TypeStudent.valueOf(dto.getTypeStudent()));
        app.setDeleted(false);
    }

    public List<ResponseApplicantList> toApplicantList(List<Applicant> applicants) {
        ResponseApplicantList res = new ResponseApplicantList();
        List<ResponseApplicantList> list = new ArrayList<>();
        for(Applicant app : applicants){
            res.setName(app.getOficialName());
            res.setSurname(app.getLastName());
            res.setDni(app.getIdentification());
            res.setPhoneNumber(app.getPhoneNumber());
            res.setEmail(app.getUser().getUsername());
            res.setGenre(String.valueOf(app.getGenre()));
            res.setBirthDate(app.getBirthDate());
            res.setTypeStudent(String.valueOf(app.getTypeStudent()));
            list.add(res);
        }
        return list;
    }

    public ResponseApplicantDto toResponseApplicant(Applicant app, String message) {
        ResponseApplicantDto dto = ResponseApplicantDto.builder()
                .id(app.getId())
                .name(app.getOficialName())
                .surname(app.getLastName())
                .dni(app.getIdentification())
                .phoneNumber(app.getPhoneNumber())
                .email(app.getUser().getUsername())
                .genre(app.getGenre())
                .birthDate(app.getBirthDate())
                .typeStudent(app.getTypeStudent())
                .message(message)
                .uri(ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/applicant/{id}")
                        .buildAndExpand(app.getUser().getUserId()).toUri())
                .build();
        return dto;
    }


}
