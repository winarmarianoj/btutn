package com.utn.bolsadetrabajo.service.impl;

import com.utn.bolsadetrabajo.dto.request.JobOfferDTO;
import com.utn.bolsadetrabajo.dto.request.JobOfferEvaluationDTO;
import com.utn.bolsadetrabajo.dto.request.PostulateDTO;
import com.utn.bolsadetrabajo.exception.JobOfferException;
import com.utn.bolsadetrabajo.mapper.JobOfferMapper;
import com.utn.bolsadetrabajo.model.*;
import com.utn.bolsadetrabajo.model.enums.State;
import com.utn.bolsadetrabajo.repository.JobApplicationRepository;
import com.utn.bolsadetrabajo.repository.JobOfferRepository;
import com.utn.bolsadetrabajo.service.interfaces.*;
import com.utn.bolsadetrabajo.service.interfaces.emails.EmailGoogleService;
import com.utn.bolsadetrabajo.service.manager.ManagerService;
import com.utn.bolsadetrabajo.service.reports.ReportLists;
import com.utn.bolsadetrabajo.util.UserConnectedService;
import com.utn.bolsadetrabajo.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class JobOfferServiceImpl implements JobOfferService {

    private static final int ZERO = 0;

    @Autowired JobOfferRepository repository;
    @Autowired JobApplicationRepository jobApplicationRepository;
    @Autowired JobOfferMapper mapper;
    @Autowired MessageSource messageSource;
    @Autowired
    EmailGoogleService emailGoogleService;
    @Autowired PublisherService publisherService;
    @Autowired ApplicantService applicantService;
    @Autowired UserConnectedService userConnectedService;
    @Autowired PersonService personService;
    @Autowired ReportLists reportLists;
    @Autowired Validator validJobOffer;
    @Autowired ManagerService managerService;

    @Override
    public ResponseEntity<?> getJobOfferById(Long id) {
        try {
            JobOffer jobOffer = getJobOffer(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                    mapper.toResponsePublisherJobOffer(jobOffer, messageSource.getMessage("joboffer.response.object.success", null, null)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("joboffer.search.failed", new Object[]{id}, null));
        }
    }

    @Override
    public ResponseEntity<?> update(Long userIdByPublisher, JobOfferDTO jobOfferDTO) {
        if(jobOfferDTO.getId() != null && jobOfferDTO.getId() > ZERO){
            JobOffer jobOffer = getJobOffer(jobOfferDTO.getId());
            return updateJobOffer(jobOffer, jobOfferDTO);
        }else {
            return create(userIdByPublisher, jobOfferDTO);
        }
    }

    private ResponseEntity<?> updateJobOffer(JobOffer jobOffer, JobOfferDTO jobOfferDTO) {
        try {
            JobOffer newJobOffer = mapper.updateJobOffer(jobOffer, jobOfferDTO);
            validJobOffer.validJobOffer(newJobOffer);
            JobOffer aux = repository.save(newJobOffer);
            return ResponseEntity.status(HttpStatus.OK).body(mapper.toResponsePublisherJobOffer(aux, messageSource.getMessage("joboffer.update.success", null, null)));
        } catch (JobOfferException e) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(messageSource.getMessage("joboffer.update.failed",new Object[] {e.getMessage()}, null));
        }
    }

    private ResponseEntity<?> create(Long userIdByPublisher, JobOfferDTO jobOfferDTO) {
        try {
            Publisher publisher = managerService.getPersonTypePublisherByIdUser(userIdByPublisher);
            JobOffer newJobOffer = mapper.toModel(jobOfferDTO, publisher);
            validJobOffer.validJobOffer(newJobOffer);
            JobOffer jobOffer = repository.save(newJobOffer);
            publisher.getJobOfferList().add(jobOffer);
            publisherService.addJobOffer(publisher);
            emailGoogleService.createEmailJobOfferPublicated(jobOffer, publisher);
            return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponsePublisherJobOffer(jobOffer, messageSource.getMessage("joboffer.created.success", null, null)));
        } catch (JobOfferException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("joboffer.created.failed",new Object[] {e.getMessage()}, null));
        }
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        try {
            JobOffer jobOffer = getJobOffer(id);
            jobOffer.setDeleted(true);
            jobOffer.setDeletedDay(LocalDate.now());
            repository.save(jobOffer);
            return ResponseEntity.status(HttpStatus.OK).body(messageSource.getMessage("joboffer.deleted.success", null, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("joboffer.deleted.failed", new Object[]{id}, null));
        }
    }

    @Override
    public ResponseEntity<?> getAll() {
        try {
            List<JobOffer> jobOffers = repository.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(mapper.toJobOfferListSimplePublisher(jobOffers));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("joboffer.all.joboffer.failed",null, null));
        }
    }

    @Override
    public ResponseEntity<?> postulate(PostulateDTO postulateDTO) {
        try {
            Applicant applicant = managerService.getPersonTypeApplicantByIdUser(postulateDTO.getApplicantID());
            JobOffer jobOffer = getJobOffer(postulateDTO.getJobofferID());
            JobApplication jobApplication = saveJobApplication(applicant, jobOffer);
            applicant.getJobApplications().add(jobApplication);
            jobOffer.getJobApplications().add(jobApplication);
            applicantService.postulateJobOffer(applicant);
            repository.save(jobOffer);
            emailGoogleService.createEmailPostulate(jobOffer, applicant);
            return ResponseEntity.status(HttpStatus.OK).body(messageSource.getMessage("applicant.postulate.success", null, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("applicant.postulate.failed", new Object[]{postulateDTO.getJobofferID()}, null));
        }
    }

    @Override
    public ResponseEntity<?> getJobOfferAllEvaluation(JobOfferEvaluationDTO jobOfferEvaluationDTO) {
        try {
            JobOffer jobOffer = repository.findById(jobOfferEvaluationDTO.getId()).get();
            JobOffer jobOfferModify = mapper.modifyJobOffer(jobOffer, jobOfferEvaluationDTO);
            if(jobOfferModify.getState().equals(State.REVIEW)) emailGoogleService.sendEmailPublisherJobOfferReview(jobOfferModify);

            JobOffer aux = repository.save(jobOfferModify);
            return ResponseEntity.status(HttpStatus.OK).body(mapper.toResponsePublisherJobOffer(aux, messageSource.getMessage("joboffer.evaluation.success", null, null)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("joboffer.evaluation.failed", new Object[]{jobOfferEvaluationDTO.getId()}, null));
        }
    }

    private JobOffer getJobOffer(Long id) {
        return repository.findById(id).get();
    }

    private JobApplication saveJobApplication (Applicant applicant, JobOffer jobOffer){
        JobApplication job = mapper.toModelJobApplication(applicant, jobOffer);
        for (JobApplication ele : applicant.getJobApplications()) {
            if (ele.getJobOffer().getTitle().equals(jobOffer.getTitle())) {
                return null;
            }
        }
        return jobApplicationRepository.save(job);
    }

}