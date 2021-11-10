package com.utn.bolsadetrabajo.dto.response;

import com.utn.bolsadetrabajo.model.enums.State;
import com.utn.bolsadetrabajo.model.enums.TypeModality;
import com.utn.bolsadetrabajo.model.enums.TypePosition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseJobOfferDto {
    private Long id;
    private String title;
    private String description;
    private String area;
    private String body;
    private String experience;
    private TypeModality modality;
    private TypePosition position;
    private String category;
    private LocalDate datePublished;
    private LocalDate modifiedDay;
    private LocalDate deletedDay;
    private boolean deleted;
    private State state;
    private String message;
}
