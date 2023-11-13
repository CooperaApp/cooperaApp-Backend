package com.coopera.cooperaApp.services.company;

import com.coopera.cooperaApp.dtos.requests.RegisterCompanyRequest;
import com.coopera.cooperaApp.dtos.response.InitializeCompanyResponse;
import com.coopera.cooperaApp.dtos.response.RegisterCooperativeResponse;
import com.coopera.cooperaApp.dtos.response.MemberResponse;
import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.models.Company;
import com.coopera.cooperaApp.repositories.CompanyRepository;
import com.coopera.cooperaApp.services.cooperative.CooperativeService;
import com.coopera.cooperaApp.services.member.MemberService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class CooperaCompanyService implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CooperativeService cooperativeService;
    private final MemberService memberService;

    public InitializeCompanyResponse registerCompany(RegisterCompanyRequest request) throws CooperaException {

    validateRegistrationRequest(request);
    MemberResponse memberResponse = memberService.registerMember(request.getMemberRequest());
    memberService.setMemberRoleToAdmin(memberResponse.getId());
    RegisterCooperativeResponse cooperativeResponse= cooperativeService.registerCooperative(request.getCooperativeRequest());
    cooperativeService.addMemberToCooperative(cooperativeResponse.getId(), memberResponse.getId());

    Company company = initializeCompany(request, cooperativeResponse.getId());
    var savedCompany = companyRepository.save(company);
    return InitializeCompanyResponse.builder().cooperativeId(savedCompany.getId()).name(savedCompany.getName()).rcNumber(savedCompany.getRcNumber()).build();
    }

    private static Company initializeCompany(RegisterCompanyRequest request, String id) {
        Company company = new Company();
        company.setAddress(request.getCompanyAddress());
        company.setName(request.getCompanyName());
        company.setRcNumber(request.getRcNumber());
        company.setCooperativeId(id);
        return company;
    }
        private void validateRegistrationRequest(RegisterCompanyRequest request) throws CooperaException {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<RegisterCompanyRequest>> violations = validator.validate(request);

            if (!violations.isEmpty()) {
                List<String> errorMessages = new ArrayList<>();
                for (ConstraintViolation<RegisterCompanyRequest> violation : violations) {
                    errorMessages.add(violation.getMessage());
                }
                throw new CooperaException("Validation error: " + String.join(", ", errorMessages));
            }
        }
}
