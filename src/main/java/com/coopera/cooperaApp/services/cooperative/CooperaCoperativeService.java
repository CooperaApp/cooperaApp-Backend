package com.coopera.cooperaApp.services.cooperative;

import com.coopera.cooperaApp.dtos.requests.RegisterCooperativeRequest;
import com.coopera.cooperaApp.dtos.response.MemberResponse;
import com.coopera.cooperaApp.dtos.response.RegisterCooperativeResponse;
import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.models.Company;
import com.coopera.cooperaApp.models.Cooperative;
import com.coopera.cooperaApp.repositories.CooperativeRepository;
import com.coopera.cooperaApp.services.member.MemberService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class CooperaCoperativeService implements CooperativeService {
    private final CooperativeRepository cooperativeRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public RegisterCooperativeResponse registerCooperative(RegisterCooperativeRequest request) throws CooperaException {
        validateRegistrationRequest(request);
        Company company = new Company();
        modelMapper.map(request, company);
        Cooperative cooperative = initializeCooperativeData(request,  company);
        cooperative.setId(generateId(request.getName()));
        Cooperative savedCooperative = cooperativeRepository.save(cooperative);
        if(savedCooperative.getId() == null) throw new CooperaException("Cooperative registration failed");
        return RegisterCooperativeResponse.builder().id(savedCooperative.getId()).name(savedCooperative.getName()).numberOfMembers(savedCooperative.getNumberOfMember()).build();

    }

    private  Cooperative initializeCooperativeData(RegisterCooperativeRequest request, Company company) {
        Cooperative cooperative = new Cooperative();
        cooperative.setName(request.getName());
        cooperative.setLogo(request.getLogo());
        cooperative.setCompany(company);
        cooperative.setPassword(passwordEncoder.encode(request.getPassword()));
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

    public String generateId(String name){
        String firstPart = name.substring(0, 3);
        int currentYear = LocalDateTime.now().getYear();
        int sizeOfRepoPlusOne = cooperativeRepository.findAll().size() + 1;
        return firstPart + "/" + currentYear + "/" + "00"+sizeOfRepoPlusOne;
    }

    @Override
    public void deleteAll() {
        cooperativeRepository.deleteAll();
    }

    @Override
    public Optional<Cooperative> findByCooperativeById(String id) {
        return cooperativeRepository.findById(id);
    }
    @Override
    public void save(Cooperative cooperative) {
        cooperativeRepository.save(cooperative);
    }
}