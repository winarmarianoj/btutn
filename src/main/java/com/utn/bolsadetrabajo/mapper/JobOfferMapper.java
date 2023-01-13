package com.utn.bolsadetrabajo.mapper;

import com.utn.bolsadetrabajo.dto.request.JobOfferDTO;
import com.utn.bolsadetrabajo.dto.request.JobOfferEvaluationDTO;
import com.utn.bolsadetrabajo.dto.request.JobOfferFlutterDTO;
import com.utn.bolsadetrabajo.dto.response.ResponseJobOfferDto;
import com.utn.bolsadetrabajo.model.*;
import com.utn.bolsadetrabajo.model.enums.State;
import com.utn.bolsadetrabajo.repository.CategoryRepository;
import com.utn.bolsadetrabajo.service.emails.EmailGoogleService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class JobOfferMapper {

    private final CategoryRepository categoryRepository;
    private final EmailGoogleService emailGoogleService;

    public JobOfferMapper(CategoryRepository categoryRepository, EmailGoogleService emailGoogleService) {
        this.categoryRepository = categoryRepository;
        this.emailGoogleService = emailGoogleService;
    }

    public JobOffer toModel(JobOfferDTO dto, Publisher publisher) {
        Category category = categoryRepository.findByName(dto.getCategory());
        JobOffer job = JobOffer.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .body(dto.getBody())
                .area(dto.getArea())
                .createDay(LocalDate.now())
                .modifiedDay(LocalDate.now())
                .deleted(false)
                .experience(dto.getExperience())
                .modality(dto.getModality())
                .position(dto.getPosition())
                .state(State.PENDING)
                .publisher(publisher)
                .category(category)
                .build();
        return job;
    }

    public ResponseJobOfferDto toResponsePublisherJobOffer(JobOffer jobOffer, String message) {
        ResponseJobOfferDto dto = ResponseJobOfferDto.builder()
                .id(jobOffer.getId())
                .title(jobOffer.getTitle())
                .description(jobOffer.getDescription())
                .body(jobOffer.getBody())
                .area(jobOffer.getArea())
                .datePublished(jobOffer.getCreateDay())
                .modifiedDay(jobOffer.getModifiedDay())
                .deletedDay(jobOffer.getDeletedDay())
                .experience(jobOffer.getExperience())
                .modality(jobOffer.getModality())
                .position(jobOffer.getPosition())
                .state(jobOffer.getState())
                .category(jobOffer.getCategory().getName())
                .message(message)
                .build();
        return dto;
    }

    public JobOffer updateJobOffer(JobOffer jobOffer, JobOfferDTO dto) {
        Category category = categoryRepository.findByName(dto.getCategory());

        jobOffer.setTitle(dto.getTitle());
        jobOffer.setDescription(dto.getDescription());
        jobOffer.setBody(dto.getBody());
        jobOffer.setArea(dto.getArea());
        jobOffer.setModifiedDay(LocalDate.now());
        jobOffer.setExperience(dto.getExperience());
        jobOffer.setModality(dto.getModality());
        jobOffer.setPosition(dto.getPosition());
        jobOffer.setCategory(category);
        return jobOffer;
    }

    public JobApplication toModelJobApplication(Applicant applicant, JobOffer jobOffer) {
        JobApplication jobApplication = JobApplication.builder()
                .applicant(applicant)
                .jobOffer(jobOffer)
                .applied(LocalDate.now())
                .deleted(false)
                .build();
        return jobApplication;
    }

    public List<ResponseJobOfferDto> toJobOfferList(List<JobOffer> jobOffers) {
        List<ResponseJobOfferDto> list = new ArrayList<>();
        for(JobOffer job : jobOffers){
            ResponseJobOfferDto res = toResponsePublisherJobOffer(job, " ");
            list.add(res);
        }
        return list;
    }

    public List<ResponseJobOfferDto> toPendingJobOfferList(List<JobOffer> jobOffers) {
        List<ResponseJobOfferDto> list = new ArrayList<>();
        for(JobOffer job : jobOffers){
            if(job.getState().equals(State.PENDING)){
                ResponseJobOfferDto res = toResponsePublisherJobOffer(job, " ");
                list.add(res);
            }
        }
        return list;
    }

    public JobOffer modifyJobOffer(JobOffer jobOffer, JobOfferEvaluationDTO dto){
        if(dto.getDecision().equals((State.APPROVED).toString())){
            jobOffer.setState(State.PUBLISHED);
        }else if(dto.getDecision().equals((State.REJECTED).toString())){
            jobOffer.setState(State.REJECTED);
        }else {
            jobOffer.setState(State.REVIEW);
        }
        return jobOffer;
    }

    public List<ResponseJobOfferDto> toJobOfferListSimplePublisher(List<JobOffer> jobOffers) {
        List<ResponseJobOfferDto> list = new ArrayList<>();
        for(JobOffer job : jobOffers){
            ResponseJobOfferDto res = toResponsePublisherJobOffer(job, " ");
            list.add(res);
        }
        return list;
    }

    public List<ResponseJobOfferDto> toJobOfferListSimplePublisherByFilter(List<JobOffer> jobOffers, Category filter) {
        List<ResponseJobOfferDto> list = new ArrayList<>();
        Category category = categoryRepository.findByName(filter.getName());
        for(JobOffer job : jobOffers){
            if(job.getCategory().getName().equals(category.getName())){
                ResponseJobOfferDto res = toResponsePublisherJobOffer(job, " ");
                list.add(res);
            }
        }
        return list;
    }

    public JobOffer updateJobOfferByFlutter(JobOffer jobOffer, JobOfferFlutterDTO dto) {
        Category category = categoryRepository.findByName(dto.getCategory());

        jobOffer.setTitle(dto.getTitle());
        jobOffer.setDescription(dto.getDescription());
        jobOffer.setBody(dto.getBody());
        jobOffer.setArea(dto.getArea());
        jobOffer.setModifiedDay(LocalDate.now());
        jobOffer.setExperience(dto.getExperience());
        jobOffer.setModality(dto.getModality());
        jobOffer.setPosition(dto.getPosition());
        jobOffer.setCategory(category);
        return jobOffer;
    }
}
