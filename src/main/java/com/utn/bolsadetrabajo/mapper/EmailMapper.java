package com.utn.bolsadetrabajo.mapper;

import com.utn.bolsadetrabajo.dto.response.ResponseEmailDto;
import com.utn.bolsadetrabajo.model.Applicant;
import com.utn.bolsadetrabajo.model.JobOffer;
import com.utn.bolsadetrabajo.model.Person;
import com.utn.bolsadetrabajo.model.Publisher;
import org.springframework.stereotype.Component;

@Component
public class EmailMapper {

    public ResponseEmailDto toModelEmailCreate(Person person, String path, String welcome) {
        String url = ""+path+"/"+person.getUser().getUsername()+"/"+person.getUser().getVerificationCode()+"";

        ResponseEmailDto res = ResponseEmailDto.builder()
                .names(person.getOficialName() + " " + person.getLastName())
                .identification(person.getIdentification())
                .email(person.getUser().getUsername())
                .phone(person.getPhoneNumber())
                .message(welcome)
                .url(url)
                .build();
        return res;
    }

    public ResponseEmailDto toModelEmailJobOffer(JobOffer jobOffer, Publisher person, String publicated, String day) {
        ResponseEmailDto res = ResponseEmailDto.builder()
                .names(person.getOficialName() + " " + person.getLastName())
                .identification(person.getIdentification())
                .email(person.getUser().getUsername())
                .message(publicated + " " + jobOffer.getTitle() + " " +
                jobOffer.getDescription() + " " + day + " " + jobOffer.getCreateDay())
                .build();
        return res;
    }

    public ResponseEmailDto toModelEmailPostulateApplicantJobOffer(JobOffer jobOffer, Applicant applicant, String postulate, String day) {
        ResponseEmailDto res = ResponseEmailDto.builder()
                .names(applicant.getOficialName() + " " + applicant.getLastName())
                .email(applicant.getUser().getUsername())
                .message(postulate + " " + jobOffer.getTitle() + " " +
                jobOffer.getDescription() + " " + day + " " + jobOffer.getCreateDay())
                .build();
        return res;
    }

    public ResponseEmailDto toModelEmailPostulatedJobOfferByPublisher(JobOffer jobOffer, Applicant applicant, String applicantPostulate, String day) {
        ResponseEmailDto res = ResponseEmailDto.builder()
                .names(applicant.getOficialName() + " " + applicant.getLastName())
                .email(jobOffer.getPublisher().getUser().getUsername())
                .message(applicantPostulate + " " + jobOffer.getTitle() + " " +
                jobOffer.getDescription() + " " + day + " " + jobOffer.getCreateDay())
                .build();
        return res;
    }

    public ResponseEmailDto toSendEmailJobOfferReview(JobOffer jobOffer, String stateReview) {
        ResponseEmailDto res = ResponseEmailDto.builder()
                .names(jobOffer.getPublisher().getOficialName() + " " + jobOffer.getPublisher().getLastName())
                .email(jobOffer.getPublisher().getUser().getUsername())
                .message(stateReview + " " + " con el tìtulo : " + " " + jobOffer.getTitle())
                .build();
        return res;
    }
}
