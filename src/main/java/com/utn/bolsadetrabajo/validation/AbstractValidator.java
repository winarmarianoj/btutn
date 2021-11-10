package com.utn.bolsadetrabajo.validation;

import com.utn.bolsadetrabajo.exception.CategoryException;
import com.utn.bolsadetrabajo.exception.JobOfferException;
import com.utn.bolsadetrabajo.exception.PersonException;
import com.utn.bolsadetrabajo.model.Category;
import com.utn.bolsadetrabajo.model.JobOffer;
import com.utn.bolsadetrabajo.model.Person;

public abstract class AbstractValidator {
    protected static final String REGEX_NAMES = "^([A-Za-zñÑ])+g+$";
    protected static final String REGEX_TEXT = "^[a-zA-ZñÑ]$";
    protected static final String REGEX_DATE = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$";

    public boolean validPerson(Person per) throws PersonException {return false;}
    public boolean validJobOffer(JobOffer jobOffer) throws JobOfferException {return false;}
    public boolean validCategory(Category category) throws CategoryException {return false;}
}
