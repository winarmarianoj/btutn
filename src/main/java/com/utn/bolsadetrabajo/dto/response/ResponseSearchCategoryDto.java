package com.utn.bolsadetrabajo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseSearchCategoryDto {
    private String name;
}
