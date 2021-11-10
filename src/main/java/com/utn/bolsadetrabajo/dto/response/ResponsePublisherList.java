package com.utn.bolsadetrabajo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ResponsePublisherList {
    private String oficialName;
    private String lastName;
    private String dni;
    private String phoneNumber;
    private String email;
    private String web;
}
