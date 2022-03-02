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
public class ResponsePublisherDto {
    private Long id;
    private URI uri;
    private String oficialName;
    private String lastName;
    private String cuit;
    private String phoneNumber;
    private String email;
    private String webPage;
    private String message;
}
