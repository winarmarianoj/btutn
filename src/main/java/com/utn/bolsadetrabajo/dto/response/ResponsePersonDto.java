package com.utn.bolsadetrabajo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.net.URI;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ResponsePersonDto {
    private Long id;
    private URI uri;
    private String name;
    private String surname;
    private String dni;
    private String phoneNumber;
    private String email;
    private String message;
}
