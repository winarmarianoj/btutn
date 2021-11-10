package com.utn.bolsadetrabajo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseApplicantList {
    private String name;
    private String surname;
    private String dni;
    private String email;
    private String phoneNumber;
    private String genre;
    private LocalDate birthDate;
    private String typeStudent;
}
