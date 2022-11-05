package com.utn.bolsadetrabajo.mapper;

import com.utn.bolsadetrabajo.dto.response.ResponseJobApplicationFlutterDto;
import com.utn.bolsadetrabajo.model.JobApplication;
import com.utn.bolsadetrabajo.model.User;
import com.utn.bolsadetrabajo.security.authentication.AuthenticationResponseByFlutter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FlutterMapper {

    public AuthenticationResponseByFlutter responseLoginUserJasonByFlutter(User user, String jwt) {
        AuthenticationResponseByFlutter auth = new AuthenticationResponseByFlutter(jwt);
        auth.setUsername(user.getUsername());
        auth.setId(user.getUserId());
        auth.setRole(String.valueOf(user.getRole().getRole()));
        return auth;
    }

    public List<ResponseJobApplicationFlutterDto> toResponseJobApplication(List<JobApplication> jobApplications) {
        List<ResponseJobApplicationFlutterDto> list = new ArrayList<>();
        for(JobApplication job : jobApplications){
            ResponseJobApplicationFlutterDto res = new ResponseJobApplicationFlutterDto();
            res.setJobOfferApplicantID(job.getId());
            res.setApplied(String.valueOf(job.getApplied()));
            res.setDeletedDay(String.valueOf(job.getDeletedDay()));
            res.setJobAppdeleted(job.isDeleted());
            res.setApplicantID(job.getApplicant().getId());
            res.setName(job.getApplicant().getOficialName());
            res.setSurname(job.getApplicant().getLastName());
            res.setDni(job.getApplicant().getIdentification());
            res.setEmail(job.getApplicant().getUser().getUsername());
            res.setPhoneNumber(job.getApplicant().getPhoneNumber());
            res.setTypeStudent(String.valueOf(job.getApplicant().getTypeStudent()));
            res.setJobOfferID(job.getJobOffer().getId());
            res.setTitle(job.getJobOffer().getTitle());
            res.setDescription(job.getJobOffer().getDescription());
            res.setArea(job.getJobOffer().getArea());
            res.setBody(job.getJobOffer().getBody());
            res.setExperience(job.getJobOffer().getExperience());
            res.setModality(String.valueOf((job.getJobOffer().getModality())));
            res.setPosition(String.valueOf(job.getJobOffer().getPosition()));
            res.setCategory(job.getJobOffer().getCategory().getName());
            res.setCategoryDescription(job.getJobOffer().getCategory().getDescription());
            res.setDatePublished(String.valueOf(job.getJobOffer().getCreateDay()));
            res.setModifiedDay(String.valueOf(job.getJobOffer().getModifiedDay()));
            res.setJobOfferDeletedDay(String.valueOf(job.getJobOffer().getDeletedDay()));
            res.setJobOfferDeleted(job.getJobOffer().isDeleted());
            res.setState(String.valueOf(job.getJobOffer().getState()));
            list.add(res);
        }
        return list;
    }
}
