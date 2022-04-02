package com.utn.bolsadetrabajo.dto.request;

import lombok.*;

@Builder
@AllArgsConstructor
@Data
@Getter
@Setter
@NoArgsConstructor
public class PostulateDTO {
    private Long applicantID;
    private Long jobofferID;
}
