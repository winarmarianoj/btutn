package com.utn.bolsadetrabajo.mapper;

import com.utn.bolsadetrabajo.dto.request.PersonDTO;
import com.utn.bolsadetrabajo.dto.response.ResponseJobApplicationFlutterDto;
import com.utn.bolsadetrabajo.dto.response.ResponseJobOfferFlutterDto;
import com.utn.bolsadetrabajo.dto.response.ResponsePersonDto;
import com.utn.bolsadetrabajo.dto.response.UserByFlutterDTO;
import com.utn.bolsadetrabajo.model.*;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class FlutterMapper {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private final DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();

    public UserByFlutterDTO responseLoginUserJasonByFlutter(User user, String jwt, Person person, Applicant app, Publisher pub) {
        UserByFlutterDTO dto = new UserByFlutterDTO(jwt);
        if(person != null){
            dto.setName(person.getOficialName());
            dto.setLastName(person.getLastName());
            dto.setIdentification(person.getIdentification());
            dto.setPhone(person.getPhoneNumber());
            dto.setUsername(user.getUsername());
            dto.setPassword(user.getPassword());
            dto.setId(user.getUserId());
            dto.setRole(String.valueOf(user.getRole().getRole()));
            dto.setConected(user.isConected());
            dto.setGenre("");
            dto.setBirthDate("");
            dto.setTypeStudent("");
            dto.setWebPage("");
        }else if(app != null){
            dto.setName(app.getOficialName());
            dto.setLastName(app.getLastName());
            dto.setIdentification(app.getIdentification());
            dto.setPhone(app.getPhoneNumber());
            dto.setUsername(user.getUsername());
            dto.setPassword(user.getPassword());
            dto.setId(user.getUserId());
            dto.setRole(String.valueOf(user.getRole().getRole()));
            dto.setConected(user.isConected());
            dto.setGenre(app.getGenre().name());
            dto.setBirthDate(app.getBirthDate().toString());
            dto.setTypeStudent(app.getTypeStudent().name());
            dto.setWebPage("");
        }else if(pub != null){
            dto.setName(pub.getOficialName());
            dto.setLastName(pub.getLastName());
            dto.setIdentification(pub.getIdentification());
            dto.setPhone(pub.getPhoneNumber());
            dto.setUsername(user.getUsername());
            dto.setPassword(user.getPassword());
            dto.setId(user.getUserId());
            dto.setRole(String.valueOf(user.getRole().getRole()));
            dto.setConected(user.isConected());
            dto.setWebPage(pub.getWebPage());
            dto.setGenre("");
            dto.setBirthDate("");
            dto.setTypeStudent("");
        }
        return dto;
    }

    public List<ResponseJobApplicationFlutterDto> toResponseJobApplication(List<JobApplication> jobApplications, String message) {
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
            res.setMessage(message);
            list.add(res);
        }
        return list;
    }

    public List<ResponseJobOfferFlutterDto> toJobOfferList(List<JobOffer> jobOffers) {
        List<ResponseJobOfferFlutterDto> list = new ArrayList<>();
        for(JobOffer job : jobOffers){
            ResponseJobOfferFlutterDto res = toResponsePublisherJobOffer(job, " ");
            list.add(res);
        }
        return list;
    }

    public ResponseJobOfferFlutterDto toResponsePublisherJobOffer(JobOffer jobOffer, String message) {
        ResponseJobOfferFlutterDto dto = ResponseJobOfferFlutterDto.builder()
                .id(jobOffer.getId())
                .title(jobOffer.getTitle())
                .description(jobOffer.getDescription())
                .body(jobOffer.getBody())
                .area(jobOffer.getArea())
                .datePublished(String.valueOf(jobOffer.getCreateDay()))
                .modifiedDay(String.valueOf(jobOffer.getModifiedDay()))
                .deletedDay(String.valueOf(jobOffer.getDeletedDay()))
                .experience(jobOffer.getExperience())
                .modality(String.valueOf(jobOffer.getModality()))
                .position(String.valueOf(jobOffer.getPosition()))
                .state(String.valueOf(jobOffer.getState()))
                .category(jobOffer.getCategory().getName())
                .message(message)
                .build();
        return dto;
    }

    public UserByFlutterDTO toResponseUpdateUser(ResponsePersonDto body, String message) {
        return dozerBeanMapper.map(body, UserByFlutterDTO.class);
    }

    public UserByFlutterDTO toResponseCreateUserByFlutterDTO(ResponsePersonDto body) {
        UserByFlutterDTO dto = new UserByFlutterDTO("");
        dto.setId(body.getId());
        dto.setName(body.getName());
        dto.setLastName(body.getSurname());
        dto.setIdentification(body.getIdentification());
        dto.setPhone(body.getPhoneNumber());
        dto.setUsername(body.getEmail());
        dto.setPassword("");
        dto.setRole(body.getRole());
        dto.setGenre(body.getGenre() != null ? body.getGenre().name() : "");
        dto.setBirthDate(body.getBirthDate() != null ? body.getBirthDate().toString() : "");
        dto.setTypeStudent(body.getTypeStudent() != null ? body.getTypeStudent().name() : "");
        dto.setWebPage(body.getWebPage() != null ? body.getWebPage() : "");
        dto.setConected(true);
        return dto;
    }

    public PersonDTO toPersonDtoByUserByFlutterDto(UserByFlutterDTO user) {
        PersonDTO dto = PersonDTO.builder()
                .name(user.getName())
                .surname(user.getLastName())
                .identification(user.getIdentification())
                .phoneNumber(user.getPhone())
                .email(user.getUsername())
                .password(user.getPassword())
                .role(user.getRole())
                .genre(user.getGenre())
                .birthDate(user.getBirthDate() != null ? LocalDate.parse(user.getBirthDate()) : null)
                .typeStudent(user.getTypeStudent() != null ? user.getTypeStudent() : "")
                .webPage(user.getWebPage() != null ? user.getWebPage() : "")
                .build();
        return dto;
    }
}
