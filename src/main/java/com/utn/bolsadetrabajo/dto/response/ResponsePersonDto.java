package com.utn.bolsadetrabajo.dto.response;

import com.utn.bolsadetrabajo.model.enums.Genre;
import com.utn.bolsadetrabajo.model.enums.TypeStudent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ResponsePersonDto {
    private Long id;
    private URI uri;
    private String name;
    private String surname;
    private String identification;
    private String phoneNumber;
    private String email;
    private String role;
    private Genre genre;
    private LocalDate birthDate;
    private TypeStudent typeStudent;
    private String webPage;
    private String message;
}
