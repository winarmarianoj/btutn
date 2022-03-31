package com.utn.bolsadetrabajo.mapper;

import com.utn.bolsadetrabajo.dto.response.ResponseJobApplicationDto;
import com.utn.bolsadetrabajo.model.JobApplication;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JobApplicationMapper {

    public List<ResponseJobApplicationDto> toResponseJobApplication(List<JobApplication> jobApplications) {
        ResponseJobApplicationDto res = new ResponseJobApplicationDto();
        List<ResponseJobApplicationDto> list = new ArrayList<>();
        for(JobApplication job : jobApplications){
            res.setJobOfferID(job.getId());
            res.setApplied(job.getApplied());
            res.setDeletedDay(job.getDeletedDay());
            res.setJobAppdeleted(job.isDeleted());
            res.setApplicantID(job.getApplicant().getId());
            res.setName(job.getApplicant().getOficialName());
            res.setSurname(job.getApplicant().getLastName());
            res.setDni(job.getApplicant().getIdentification());
            res.setEmail(job.getApplicant().getUser().getUsername());
            res.setPhoneNumber(job.getApplicant().getPhoneNumber());
            res.setTypeStudent(String.valueOf(job.getApplicant().getTypeStudent()));
            res.setTitle(job.getJobOffer().getTitle());
            res.setDescription(job.getJobOffer().getDescription());
            res.setArea(job.getJobOffer().getArea());
            res.setBody(job.getJobOffer().getBody());
            res.setExperience(job.getJobOffer().getExperience());
            res.setModality((job.getJobOffer().getModality()));
            res.setPosition(job.getJobOffer().getPosition());
            res.setDatePublished(job.getJobOffer().getCreateDay());
            res.setModifiedDay(job.getJobOffer().getModifiedDay());
            res.setJobOfferDeletedDay(job.getJobOffer().getDeletedDay());
            res.setJobOfferDeleted(job.getJobOffer().isDeleted());
            res.setState(job.getJobOffer().getState());
            list.add(res);
        }
        return list;
    }

}
