package com.utn.bolsadetrabajo.service.interfaces.emails;

import com.utn.bolsadetrabajo.exception.PersonException;
import com.utn.bolsadetrabajo.model.Applicant;
import com.utn.bolsadetrabajo.model.JobOffer;
import com.utn.bolsadetrabajo.model.Person;
import com.utn.bolsadetrabajo.model.Publisher;

public interface IEmailService {

    void createEmailPerson(Person aux) throws PersonException;

    void createEmailJobOfferPublicated(JobOffer jobOffer, Publisher publisher);

    void createEmailPostulate(JobOffer jobOffer, Applicant applicant);

    void sendEmailPublisherJobOfferReview(JobOffer jobOffer);
}
