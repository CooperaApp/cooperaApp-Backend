package com.coopera.cooperaApp.services.cooperative;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.coopera.cooperaApp.dtos.requests.EmailDetails;
import com.coopera.cooperaApp.dtos.requests.PasswordResetRequest;
import com.coopera.cooperaApp.dtos.requests.RegisterCooperativeRequest;
import com.coopera.cooperaApp.dtos.response.RegisterCooperativeResponse;
import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.models.Company;
import com.coopera.cooperaApp.models.Cooperative;
import com.coopera.cooperaApp.repositories.CooperativeRepository;
import com.coopera.cooperaApp.services.Mail.MailService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

import static com.coopera.cooperaApp.utilities.AppUtils.*;

@Service
@AllArgsConstructor
@Slf4j
public class CooperaCoperativeService implements CooperativeService {
    private final CooperativeRepository cooperativeRepository;
    private final MailService mailService;
    private final ModelMapper modelMapper;
    public static final String JWT_SECRET = "${jwt.secret}";
    private PasswordEncoder passwordEncoder;


    public RegisterCooperativeResponse registerCooperative(RegisterCooperativeRequest request) throws CooperaException {
        validateRegistrationRequest(request);
        if(cooperativeRepository.findByEmail(request.getEmail()) != null) throw new CooperaException("Cooperative with eamil "+request.getEmail()+ " already exists");
        Company company = new Company();
        modelMapper.map(request, company);
        Cooperative cooperative = initializeCooperativeData(request,  company);
        cooperative.setId(generateId(request.getName()));
        Cooperative savedCooperative = cooperativeRepository.save(cooperative);
        if(savedCooperative.getId() == null) throw new CooperaException("Cooperative registration failed");
        return RegisterCooperativeResponse.builder().id(savedCooperative.getId()).name(savedCooperative.getName()).numberOfMembers(savedCooperative.getNumberOfMember()).build();

    }

    private static Cooperative initializeCooperativeData(RegisterCooperativeRequest request, Company company) {
        Cooperative cooperative = new Cooperative();
        cooperative.setEmail(request.getEmail());
        cooperative.setName(request.getName());
        cooperative.setLogo(request.getLogo());
        cooperative.setCompany(company);
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
    public Cooperative findCooperativeByMail(String mail) {
        return cooperativeRepository.findByEmail(mail);
    }
    @Override
    public Object forgotPassword(String email) throws CooperaException {
      Cooperative cooperative = findCooperativeByMail(email);
      if (cooperative == null) {
          throw new CooperaException(" Cooperative with email " + email + " does not exist");
      }
        EmailDetails emailDetails = new EmailDetails();
          emailDetails.setSubject("Verify your account");
          emailDetails.setRecipient(cooperative.getEmail());
          emailDetails.setMsgBody(String.format(VERIFY_ACCOUNT, cooperative.getName(), generateLink(cooperative.getId())));
        return mailService.sendEmail(emailDetails);
    }
    private static String generateLink(String cooperativeId) {
        return FRONTEND_URL+"reset-password?token=" +
                JWT.create()
                        .withIssuedAt(Instant.now())
                        .withClaim("cooperativeId", cooperativeId)
                        .withExpiresAt(Instant.now().plusSeconds(600L))
                        .sign(Algorithm.HMAC512(JWT_SECRET.getBytes()));
    }

    @Override
    public void save(Cooperative cooperative) {
        cooperativeRepository.save(cooperative);
    }

    public String resetPassword(PasswordResetRequest passwordResetRequest) throws CooperaException {
        if(!Objects.equals(passwordResetRequest.getNewPassword(), passwordResetRequest.getConfirmPassword())) throw  new CooperaException("Password do not match!");
        String token = passwordResetRequest.getToken();
        String newPassword = passwordResetRequest.getNewPassword();
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(JWT_SECRET.getBytes()))
                .build().verify(token);
        if (decodedJWT == null) throw new CooperaException("Password Reset Failed");
        Claim claim = decodedJWT.getClaim("cooperativeId");
        String id = claim.asString();
        Cooperative cooperative = cooperativeRepository.findById(id).orElseThrow(() ->
                new CooperaException(" Cooperative with email " + id+ " does not exist"));
        cooperative.setPassword(passwordEncoder.encode(newPassword));
        cooperativeRepository.save(cooperative);
        return PASSWORD_RESET;
    }
}