package com.utn.bolsadetrabajo.validation;

import com.utn.bolsadetrabajo.dto.request.ApplicantDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class AgeValidate {
    public boolean ageValidateApplicant(ApplicantDTO applicant, int age){
        Long diff = applicant.getBirthDate().until(LocalDate.now(), ChronoUnit.YEARS);
        return diff >= age;
    }
}
