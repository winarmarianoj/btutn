package com.utn.bolsadetrabajo.dto.request;

import lombok.*;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Builder
@AllArgsConstructor
@Data
@Getter
@Setter
@NoArgsConstructor
public class PublisherDTO {

    @NotNull(message = "Nombre no puede estar vacìo.")
    @Size(min = 4, max = 64, message = "Name size should be between 4-64 characters.")
    private String oficialName;

    @NotNull(message = "Nombre no puede estar vacìo.")
    @Size(min = 4, max = 64, message = "Name size should be between 4-64 characters.")
    private String lastName;

    @NotNull(message = "CUIT/CUIL no puede estar vacìo.") //@Pattern(regexp = "^[0-9]{2}-[0-9]{8}-[0-9]{1}$", message = "Invalid CUIT/CUIL")
    private String cuit;

    @NotNull(message = "Email no puede estar vacìo.")
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Invalid email")
    @Email
    private String email;

    @NotNull(message = "Password no puede estar vacìo.")
    @Pattern(regexp = "(?=^.{8,}$)((?=.*\\d)(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$",
            message = "Invalid password. Must have one upper case letter, one lower case letter," +
                    "one number, one special character and at least 8 characters long")
    private String password;

    @NotNull(message = "Telèfono no puede estar vacìo.")
    @Size(min = 8, max = 20, message = "Invalid phone number")
    private String phoneNumber;

    @NotNull(message = "URL no puede estar vacìo.")
    @URL()
    private String webPage;

}
