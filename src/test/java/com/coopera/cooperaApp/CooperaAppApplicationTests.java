package com.coopera.cooperaApp;

import com.coopera.cooperaApp.dtos.requests.UpdateCooperativeRequest;
import com.coopera.cooperaApp.models.AccountingEntry;
import com.coopera.cooperaApp.repositories.CooperativeRepository;
import com.coopera.cooperaApp.repositories.LoanRepository;
import com.coopera.cooperaApp.repositories.MemberRepository;
import com.coopera.cooperaApp.services.cooperative.CooperativeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CooperaAppApplicationTests {
    @Autowired
    public LoanRepository loanRepository;
    @Autowired
    public MemberRepository memberRepository;
	@Autowired
	public CooperativeService cooperativeService;
    @Autowired
    public CooperativeRepository cooperativeRepository;
    @Autowired
    public ObjectMapper objectMapper;

    @Test
    void contextLoads() throws JsonProcessingException {
		for (var each:loanRepository.findAll()) {
			if (each.getMemberId() == null){
				each.setMemberId("Sam/2024/001/2");
				loanRepository.save(each);
			}
		}
    }
    @Test
    void coop() throws JsonProcessingException {
		for (var each:cooperativeRepository.findAll()) {
			if (each.getAccountingEntry().getInterestRate() == null){
				each.getAccountingEntry().setInterestRate(0.5);
				cooperativeRepository.save(each);
				System.out.println(cooperativeRepository.findById(each.getId()));
			}
		}
    }
    @Test
    void memberTest() {

		int count = 1;
		for (var each:loanRepository.findAll()) {
			if (each.getMemberName() == null){
				if (each.getId().equalsIgnoreCase("Sam/2024/001/2")){
					System.out.println(count++);
					each.setMemberName("Amuludun Inumiodun");
					loanRepository.save(each);
				}
				else {
					System.out.println(count++);
					each.setMemberName("John Doe");
					loanRepository.save(each);
				}
			}
		}
    }

}
