package com.utn.bolsadetrabajo.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseEmailDto {
    private String names;
    private String identification;
    private String email;
    private String phone;
    private String message;
    private String url;
}
