package com.coopera.cooperaApp.services.cooperative;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.coopera.cooperaApp.dtos.requests.EmailDetails;
import com.coopera.cooperaApp.dtos.requests.PasswordResetRequest;
import com.coopera.cooperaApp.dtos.CooperativeDashboardStatistic;
import com.coopera.cooperaApp.dtos.requests.RegisterCooperativeRequest;
import com.coopera.cooperaApp.dtos.requests.UpdateCooperativeRequest;
import com.coopera.cooperaApp.dtos.response.CooperativeResponse;
import com.coopera.cooperaApp.dtos.response.RegisterCooperativeResponse;
import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.models.Company;
import com.coopera.cooperaApp.models.Cooperative;
import com.coopera.cooperaApp.repositories.CooperativeRepository;
import com.coopera.cooperaApp.security.JwtUtil;
import com.coopera.cooperaApp.services.Mail.MailService;
import com.coopera.cooperaApp.services.SavingsServices.SavingsService;
import com.coopera.cooperaApp.services.loanServices.LoanService;
import com.coopera.cooperaApp.services.member.MemberService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.github.fge.jackson.jsonpointer.JsonPointerException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchOperation;
import com.github.fge.jsonpatch.ReplaceOperation;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.Instant;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static com.coopera.cooperaApp.utilities.AppUtils.*;

import static com.coopera.cooperaApp.utilities.AppUtils.retrieveCooperativeEmail;

@Service
@AllArgsConstructor
@Slf4j
public class CooperaCoperativeService implements CooperativeService {
    private final CooperativeRepository cooperativeRepository;
    private final MailService mailService;
    private final ModelMapper modelMapper;
    private final JwtUtil jwtUtil;
    private PasswordEncoder passwordEncoder;


    @Override
    public RegisterCooperativeResponse registerCooperative(RegisterCooperativeRequest request, MemberService memberService) throws CooperaException {
        validateRegistrationRequest(request);
        if(cooperativeRepository.findByEmail(request.getEmail()) != null) throw new CooperaException("Cooperative with eamil "+request.getEmail()+ " already exists");
        Company company = new Company();
        modelMapper.map(request, company);
        Cooperative cooperative = initializeCooperativeData(request,  company);
        cooperative.setId(generateId(request.getName()));

        System.out.println(cooperative);

        Cooperative savedCooperative = cooperativeRepository.save(cooperative);
        if(savedCooperative.getId() == null) throw new CooperaException("Cooperative registration failed");
        Long numberOfCooperativeMembers = memberService.getNumberOfMembersByCooperativeId(savedCooperative.getId());
        return RegisterCooperativeResponse.builder()
                .name(savedCooperative.getName())
                .numberOfMembers(numberOfCooperativeMembers).build();

    }

    private  Cooperative initializeCooperativeData(RegisterCooperativeRequest request, Company company) {
        Cooperative cooperative = new Cooperative();
        cooperative.setEmail(request.getEmail());
        cooperative.setName(request.getName());
        cooperative.setLogo(request.getLogo());
        cooperative.setCompany(company);
        cooperative.setPassword(passwordEncoder.encode(request.getPassword()));
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
    public Optional<Cooperative> findById(String id) {
        return cooperativeRepository.findById(id);
    }

    @Override
    public Optional<Cooperative> findByEmail(String email) {
        return Optional.ofNullable(cooperativeRepository.findByEmail(email));
    }

    @Override
    public Cooperative findCooperativeByMail(String mail) {
        return cooperativeRepository.findByEmail(mail);
    }
    @Override
    public String forgotPassword(String email) throws CooperaException {
        Cooperative cooperative = findCooperativeByMail(email);
        if (cooperative == null) {
            throw new CooperaException(String.format(INVALID_COOPERATIVE_EMAIL,email));
        }
        String link =generateLink(cooperative.getId());
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setSubject(ACCOUNT_VERIFICATION_SUBJECT );
        emailDetails.setRecipient(cooperative.getEmail());
        emailDetails.setMsgBody(String.format(VERIFY_ACCOUNT,cooperative.getName(),link));
        return mailService.mimeMessage(emailDetails);
    }
    private String generateLink(String cooperativeId) {
        return FRONTEND_URL+"reset-password?token=" +
                JWT.create()
                        .withIssuedAt(Instant.now())
                        .withExpiresAt(Instant.now().plusSeconds(600L))
                        .withClaim("cooperativeId", cooperativeId)
                        .sign(Algorithm.HMAC512(jwtUtil.getSecret().getBytes()));
    }

    @Override
    public void save(Cooperative cooperative) {
        cooperativeRepository.save(cooperative);
    }

    public String resetPassword(PasswordResetRequest passwordResetRequest) throws CooperaException {
        if(!Objects.equals(passwordResetRequest.getNewPassword(), passwordResetRequest.getConfirmPassword())) throw  new CooperaException("Password do not match!");
        String newPassword = passwordResetRequest.getNewPassword();

        String token = passwordResetRequest.getToken();
        DecodedJWT decodedJWT =jwtUtil.validateToken(token);
        if (decodedJWT == null) throw new CooperaException(PASSWORD_RESET_FAILED);
        Claim claim = decodedJWT.getClaim("cooperativeId");
        String id = claim.asString();
        Cooperative cooperative = cooperativeRepository.findById(id).orElseThrow(() ->
                new CooperaException(String.format(COOPERATIVE_WITH_ID_NOT_FOUND ,id)));
        cooperative.setPassword(passwordEncoder.encode(newPassword));
        cooperativeRepository.save(cooperative);
        return PASSWORD_RESET_SUCCESSFUL;
    }

    @Override
    public CooperativeDashboardStatistic getDashboardStatistics(SavingsService savingsService, LoanService loanService) {
        System.out.println("Reached");
        String cooperativeId = retrieveCooperativeEmail();
        System.out.println("cooperativeId::>> "+cooperativeId);
        CooperativeDashboardStatistic cooperativeDashboardStatistic = new CooperativeDashboardStatistic();
        BigDecimal totalCooperativeSavings = savingsService.calculateTotalCooperativeSavings(cooperativeId);
        BigDecimal totalDisbursedLoan = loanService.calculateTotalDisbursedLoan(cooperativeId);
        BigDecimal accountBalance = totalCooperativeSavings.subtract(totalDisbursedLoan);
        cooperativeDashboardStatistic.setAccountBalance(accountBalance);
        cooperativeDashboardStatistic.setLoanDisbursed(totalDisbursedLoan);
        cooperativeDashboardStatistic.setTotalSavings(totalCooperativeSavings);
        BigDecimal totalRepaidLoan = loanService.calculateTotalRepaidLoan(cooperativeId);
        cooperativeDashboardStatistic.setLoanRepaid(totalRepaidLoan);
        return cooperativeDashboardStatistic;
    }

    private BigDecimal calculateCooperativeAccountBalance(String cooperativeId, SavingsService savingsService, LoanService loanService) {
        BigDecimal totalCooperativeSavings = savingsService.calculateTotalCooperativeSavings(cooperativeId);
        BigDecimal totalDisbursedLoan = loanService.calculateTotalDisbursedLoan(cooperativeId);
        return null;

    }
    public CooperativeResponse updateCooperativeDetails(UpdateCooperativeRequest updateRequest)
            throws CooperaException, JsonPointerException, IllegalAccessException {
        String id= retrieveCooperativeEmail();
        Optional<Cooperative> foundCooperative = cooperativeRepository.findById(id);
        Cooperative cooperative = foundCooperative.orElseThrow(() ->
                new CooperaException(String.format(COOPERATIVE_WITH_ID_NOT_FOUND, id)));

        JsonPatch jsonPatch = buildUpdatePatch(updateRequest);
        Cooperative updatedCooperative = updateCooperative(cooperative, jsonPatch);
        cooperativeRepository.save(updatedCooperative);

        return CooperativeResponse.builder()
                .id(updatedCooperative.getId())
                .logo(updatedCooperative.getLogo())
                .name(updatedCooperative.getName())
                .email(updatedCooperative.getEmail())
                .dateCreated(updatedCooperative.getDateCreated())
                .interestRate(updatedCooperative.getAccountingEntry().getInterestRate())
                .loanEligibilityRate(updatedCooperative.getAccountingEntry().getLoanEligibilityRate())
                .companyName(updatedCooperative.getCompany().getCompanyName())
                .address(updatedCooperative.getCompany().getAddress())
                .build();

    }

    private JsonPatch buildUpdatePatch(UpdateCooperativeRequest updateRequest) throws IllegalAccessException, JsonPointerException {
        List<JsonPatchOperation> operations = new ArrayList<>();
        List<String> updateFields = List.of("name", "email", "logo", "companyName", "address", "interestRate", "loanEligibilityRate");
        Field[] fields = updateRequest.getClass().getDeclaredFields();

        buildPatchOperations(updateRequest, operations, updateFields, fields);
        return new JsonPatch(operations);
    }

    private static void buildPatchOperations(UpdateCooperativeRequest updateRequest, List<JsonPatchOperation> operations, List<String> updateFields, Field[] fields) throws IllegalAccessException, JsonPointerException {
        for (Field field : fields) {
            field.setAccessible(true);

            if (field.get(updateRequest)!=null&& updateFields.contains(field.getName())){
                if (field.getName().contains("company")||field.getName().contains("address")){
                    var operation = new ReplaceOperation(
                            new JsonPointer("/company/"+field.getName()),
                            new TextNode(field.get(updateRequest).toString())
                    );
                    operations.add(operation);
                }else if(field.getName().contains("Rate")){
                    var operation = new ReplaceOperation(
                            new JsonPointer("/accountingEntry/"+field.getName()),
                            new TextNode(field.get(updateRequest).toString())
                    );
                    operations.add(operation);
                }
                else{
                    var operation = new ReplaceOperation(
                            new JsonPointer("/" + field.getName()),
                            new TextNode(field.get(updateRequest).toString())
                    );
                    operations.add(operation);
                }
            }
        }
    }

    private Cooperative updateCooperative(Cooperative cooperative, JsonPatch jsonPatch) throws CooperaException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode cooperativeNode = mapper.convertValue(cooperative, JsonNode.class);
        try {
            JsonNode updatedNode = jsonPatch.apply(cooperativeNode);
            Cooperative updatedCooperative = mapper.convertValue(updatedNode, Cooperative.class);
            updatedCooperative.setId(cooperative.getId());
            updatedCooperative.setPassword(cooperative.getPassword());
            updatedCooperative.setDateCreated(cooperative.getDateCreated());

            return updatedCooperative;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CooperaException(COOPERATIVE_UPDATE_FAILED);
        }
    }

}