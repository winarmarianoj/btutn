package com.utn.bolsadetrabajo.service.emails.impl;

import com.utn.bolsadetrabajo.dto.response.ResponseEmailDto;
import com.utn.bolsadetrabajo.mapper.EmailMapper;
import com.utn.bolsadetrabajo.model.Applicant;
import com.utn.bolsadetrabajo.model.JobOffer;
import com.utn.bolsadetrabajo.model.Person;
import com.utn.bolsadetrabajo.model.Publisher;
import com.utn.bolsadetrabajo.service.emails.EmailGoogleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailGoogleServiceImpl implements EmailGoogleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailGoogleServiceImpl.class);
    private static final String EMAIL_WELCOME = "Bienvenido a la Bolsa de Trabajo del CUVL-UTN";
    private static final String PATH_BASE = "http://localhost:8082/auth/activate";
    private static final String PUBLICATED = "Ha publicado el siguiente aviso : ";
    private static final String DAY = " el dìa ";
    private static final String POSTULATE = "Ud. se ha postulado al siguiente aviso : ";
    private static final String APPLICANT_POSTULATE = "Se ha postulado al siguiente aviso : ";
    private static final String STATE_REVIEW = "Tiene un aviso en estado de revisiòn";

    private final JavaMailSender sender;
    private final EmailMapper emailMapper;

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Autowired
    public EmailGoogleServiceImpl(JavaMailSender sender, EmailMapper emailMapper) {
        this.sender = sender;
        this.emailMapper = emailMapper;
    }

    @Override
    public void createEmailPerson(Person app){
        ResponseEmailDto email = emailMapper.toModelEmailCreate(app, PATH_BASE, EMAIL_WELCOME);
        LOGGER.info("Construyendo el cuerpo del mail a enviar al Applicant...");
        String bodyText = "Denominaciòn y Nombres :" + " " + email.getNames()
                + " ." + " " + "Presione este link para Activar su cuenta : " + " " + email.getUrl();
        String subject = "Bienvenido a Bolsa de Trabajo CUVL-UTN 2021";
        sendEmail(email, bodyText, subject);
    }

    @Override
    public void createEmailJobOfferPublicated(JobOffer jobOffer, Publisher publisher) {
        ResponseEmailDto email = emailMapper.toModelEmailJobOffer(jobOffer, publisher, PUBLICATED, DAY);
        LOGGER.info("Construyendo el cuerpo para enviar el Mail al Publicador del aviso...");
        String bodyText = "Denominaciòn y Nombres :" + " " + email.getNames()
                + " ." + " " + "Se ha publicado su aviso con el Tìtulo : " + " " + jobOffer.getTitle() + " " +
                " y la Descripciòn : " + " " + jobOffer.getDescription() + " .";
        String subject = "Publicaciòn de su Aviso-Bolsa de Trabajo UTN";
        sendEmail(email, bodyText, subject);
    }

    @Override
    public void createEmailPostulate(JobOffer jobOffer, Applicant applicant) {
        ResponseEmailDto emailApplicant = emailMapper.toModelEmailPostulateApplicantJobOffer(jobOffer, applicant, POSTULATE, DAY);
        LOGGER.info("Construyendo el cuerpo para enviar el Mail al Postulante sobre el aviso que se aplico...");
        String bodyText = "Denominaciòn y Nombres :" + " " + emailApplicant.getNames()
                + " ." + " " + "Se ha postulado al aviso con el Tìtulo : " + " " + jobOffer.getTitle() + " " +
                " y la Descripciòn : " + " " + jobOffer.getDescription() + " .";
        String subject = "Te postulaste al Aviso-UTN";
        sendEmail(emailApplicant, bodyText, subject);

        ResponseEmailDto emailPublisher = emailMapper.toModelEmailPostulatedJobOfferByPublisher(jobOffer, applicant, APPLICANT_POSTULATE, DAY);
        LOGGER.info("Construyendo el cuerpo para enviar el Mail al Publicador del aviso que se han postulado...");
        String bodyTextJob = "Denominaciòn y Nombres :" + " " + emailPublisher.getNames()
                + " ." + " " + "Se han postulado a su Aviso con el Tìtulo : " + " " + jobOffer.getTitle() + " " +
                " y la Descripciòn : " + " " + jobOffer.getDescription() + " .";
        String subjectJob = "Se han Postulado a su Aviso";
        sendEmail(emailPublisher, bodyTextJob, subjectJob);
    }

    @Override
    public void sendEmailPublisherJobOfferReview(JobOffer jobOffer) {
        ResponseEmailDto jobOfferReview = emailMapper.toSendEmailJobOfferReview(jobOffer, STATE_REVIEW);
        String bodyReview = "Tiene un aviso para reveer";
        String subjectReview = "Un aviso para revisar antes de ser publicado";
        sendEmail(jobOfferReview, bodyReview, subjectReview);
    }

    private void sendEmail(ResponseEmailDto email, String body, String subject){
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setTo(email.getEmail());
            helper.setText(body, true);
            helper.setSubject(subject);
            sender.send(message);
            LOGGER.info("Mail enviado!");
        } catch (MessagingException e) {
            LOGGER.error("Hubo un error al enviar el mail.");
        }
    }


}
