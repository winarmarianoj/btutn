package com.utn.bolsadetrabajo.validation;

import com.utn.bolsadetrabajo.exception.CategoryException;
import com.utn.bolsadetrabajo.exception.JobOfferException;
import com.utn.bolsadetrabajo.exception.PersonException;
import com.utn.bolsadetrabajo.model.Applicant;
import com.utn.bolsadetrabajo.model.Category;
import com.utn.bolsadetrabajo.model.JobOffer;
import com.utn.bolsadetrabajo.model.Person;
import com.utn.bolsadetrabajo.model.enums.Roles;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Component
public class Validator extends AbstractValidator {

    public boolean validPerson(Person per) throws PersonException {
        Map<String, String> list = getResponseText(per);
        if(per.getUser().getRole().getRole().equals(Roles.APPLICANT)){
            Applicant app = (Applicant) per;
            list.put(app.getBirthDate().toString(), !Pattern.matches(REGEX_DATE, getLocalDateFormat(app)) ? "1" : "Fecha de Nacimiento es incorrecto o invalido");
        }
        ResponseValidatorDto dto = getResultValidate(list);
        if(!dto.isResult()){throw new PersonException(dto.getResponse());}

        return dto.isResult();
    }

    public boolean validCategory(Category cat) throws CategoryException {
        Map<String, String> list = getResponseText(cat);
        ResponseValidatorDto dto = getResultValidate(list);
        if(!dto.isResult()){throw new CategoryException(dto.getResponse());}

        return dto.isResult();
    }

    public boolean validJobOffer(JobOffer jobOffer) throws JobOfferException {
        /*Map<String, String> list = getResponseText(jobOffer);
        ResponseValidatorDto dto = getResultValidate(list);
        if(!dto.isResult()){throw new JobOfferException(dto.getResponse());}

        return dto.isResult();*/
        return true;
    }

    private ResponseValidatorDto getResultValidate(Map<String, String> list) {
        ResponseValidatorDto dto = new ResponseValidatorDto();
        dto.setResult(true);
        for (Map.Entry<String, String> ele : list.entrySet()) {
            if (!ele.getValue().equals("1")) {
                dto.setResponse("El campo ingresado : " + " " + ele.getKey() + " / el resultado es : " + " " + ele.getValue());
                dto.setResult(false);
                break;
            }
        }
        return dto;
    }

    private Map<String, String> getResponseText(JobOffer jobOffer) {
        Map<String, String> list = new HashMap<>();
        list.put(jobOffer.getTitle(), jobOffer.getTitle() != null || !Pattern.matches(REGEX_TEXT, jobOffer.getTitle()) ? "Esta todo OK!" : "El Nombre o Nombre Oficial es incorrecto o invalido");
        list.put(jobOffer.getDescription(), jobOffer.getDescription() != null || !Pattern.matches(REGEX_TEXT, jobOffer.getDescription()) ? "Esta todo OK!" : "El Apellido o Nombre Representativo es incorrecto o invalido");
        list.put(jobOffer.getArea(), jobOffer.getArea() != null || !Pattern.matches(REGEX_TEXT, jobOffer.getArea()) ? "Esta todo OK!" : "El Area es incorrecto o invalido");
        list.put(jobOffer.getBody(), jobOffer.getBody() != null || !Pattern.matches(REGEX_TEXT, jobOffer.getBody()) ? "Esta todo OK!" : "El Cuerpo es incorrecto o invalido");
        list.put(jobOffer.getExperience(), jobOffer.getExperience() != null || !Pattern.matches(REGEX_TEXT, jobOffer.getExperience()) ? "Esta todo OK!" : "La Experiencia es incorrecto o invalido");
        list.put(jobOffer.getCategory().getName(), jobOffer.getCategory().getName() != null || !Pattern.matches(REGEX_TEXT, jobOffer.getCategory().getName()) ? "Esta todo OK!" : "La Categoria es incorrecto o invalido");

        return list;
    }

    private Map<String, String> getResponseText(Category category) {
        Map<String, String> list = new HashMap<>();
        list.put(category.getName(), category.getName() != null || !Pattern.matches(REGEX_NAMES, category.getName()) ? "1" : "El Nombre de la Categoria es incorrecto o invalido");
        list.put(category.getDescription(), category.getDescription() != null || !Pattern.matches(REGEX_NAMES, category.getDescription()) ? "1" : "La descripcion es incorrecto o invalido");

        return list;
    }

    private String getLocalDateFormat(Applicant app) {
        return app.getBirthDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }

    private  Map<String, String>  getResponseText(Person per) {
        Map<String, String> list = new HashMap<>();
        list.put(per.getOficialName(), per.getOficialName() != null || !Pattern.matches(REGEX_NAMES, per.getOficialName()) ? "1" : "El Nombre o Nombre Oficial es incorrecto o invalido");
        list.put(per.getLastName(), per.getLastName() != null || !Pattern.matches(REGEX_NAMES, per.getLastName()) ? "1" : "El Apellido o Nombre Representativo es incorrecto o invalido");
        list.put(per.getPhoneNumber(), StringUtils.isNumeric(per.getPhoneNumber()) ? "1" : "El Telèfono es incorrecto o invalido");
        list.put(per.getIdentification(), StringUtils.isNumeric(per.getIdentification()) ? "1" : "Su Identificacion es incorrecto o invalido");

        return list;
    }

}
