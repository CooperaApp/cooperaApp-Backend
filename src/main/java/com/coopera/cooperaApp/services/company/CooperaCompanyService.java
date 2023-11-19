package com.coopera.cooperaApp.services.company;

import com.coopera.cooperaApp.dtos.requests.RegisterCompanyRequest;
import com.coopera.cooperaApp.dtos.requests.RegisterCooperativeRequest;
import com.coopera.cooperaApp.dtos.response.InitializeCompanyResponse;
import com.coopera.cooperaApp.dtos.response.RegisterCooperativeResponse;
import com.coopera.cooperaApp.dtos.response.MemberResponse;
import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.models.Company;
import com.coopera.cooperaApp.models.Cooperative;
import com.coopera.cooperaApp.repositories.CompanyRepository;
import com.coopera.cooperaApp.repositories.CooperativeRepository;
import com.coopera.cooperaApp.services.cooperative.CooperativeService;
import com.coopera.cooperaApp.services.member.MemberService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
@AllArgsConstructor
@Slf4j
public class CooperaCompanyService implements CompanyService {

    private final CooperativeRepository cooperativeRepository;
    private final MemberService memberService;
    private final ModelMapper modelMapper;

    public RegisterCooperativeResponse registerCooperative(RegisterCooperativeRequest request) throws CooperaException {
        validateRegistrationRequest(request);
        MemberResponse memberResponse = memberService.registerMember(request.getMemberRequest());
        memberService.setMemberRoleToAdmin(memberResponse.getId());
        Company company = new Company();
        modelMapper.map(request, company);
        Cooperative cooperative = initializeCooperativeData(request, memberResponse, company);
        Cooperative savedCooperative = cooperativeRepository.save(cooperative);
        if(savedCooperative.getId() == null) throw new CooperaException("Cooperative registration failed");
        return RegisterCooperativeResponse.builder().id(savedCooperative.getId()).name(savedCooperative.getName()).numberOfMembers(savedCooperative.getNumberOfMember()).build();
    }

    private static Cooperative initializeCooperativeData(RegisterCooperativeRequest request, MemberResponse memberResponse, Company company) {
        Cooperative cooperative = new Cooperative();
        cooperative.setName(request.getName());
        cooperative.setLogo(request.getLogo());
        cooperative.setCompany(company);
        cooperative.getMembersId().add(memberResponse.getId());
        cooperative.setNumberOfMember(cooperative.getMembersId().size());
        cooperative.setDateCreated(LocalDateTime.now());
        return cooperative;
    }

    private void validateRegistrationRequest(RegisterCooperativeRequest request) throws CooperaException {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<RegisterCooperativeRequest>> violations = validator.validate(request);

        if (!violations.isEmpty()) {
            List<String> errorMessages = new ArrayList<>();
            for (ConstraintViolation<RegisterCooperativeRequest> violation : violations) {
                errorMessages.add(violation.getMessage());
            }
            throw new CooperaException("Validation error: " + String.join(", ", errorMessages));
        }
    }

    @Override
    public void deleteAll() {
        cooperativeRepository.deleteAll();
    }
}
