package com.coopera.cooperaApp.services.member;

import com.coopera.cooperaApp.dtos.requests.RegisterMemberRequest;
import com.coopera.cooperaApp.dtos.response.MemberResponse;
import com.coopera.cooperaApp.enums.Role;
import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.models.Cooperative;
import com.coopera.cooperaApp.models.Member;
import com.coopera.cooperaApp.repositories.MemberRepository;
import com.coopera.cooperaApp.services.cooperative.CooperaCoperativeService;
import com.coopera.cooperaApp.services.cooperative.CooperativeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CooperaMemberService implements MemberService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CooperativeService cooperativeService;

    public MemberResponse registerMember(RegisterMemberRequest request) throws CooperaException {
        checkIfMemberExistByEmail(request);
        Member newMember = initializeNewMember(request);
        var savedMember = memberRepository.save(newMember);
      if (savedMember.getId() == null) throw new CooperaException("Member Registration failed");
      Optional<Cooperative> optionalCooperative = cooperativeService.findByCooperativeById(request.getCooperativeId());
       Cooperative cooperative = optionalCooperative.orElseThrow(() -> new CooperaException(String.format("Cooperative with %s id not found", request.getCooperativeId())));
      cooperative.getMembersId().add(request.getMemberId());
      cooperativeService.save(cooperative);
      return MemberResponse.builder().id(savedMember.getId()).role(savedMember.getRoles()).name(savedMember.getFirstName() + " "+ savedMember.getLastName()).build();
    }


    public MemberResponse setMemberRoleToAdmin(String id) throws CooperaException {
       var foundMember  = memberRepository.findById(id);
       foundMember.orElseThrow(()->new CooperaException("Could not find member with " + id ));
       foundMember.get().getRoles().add(Role.ADMIN);
       var savedMember = memberRepository.save(foundMember.get());
       return MemberResponse.builder().id(savedMember.getId()).role(savedMember.getRoles()).name(savedMember.getFirstName() + " "+ savedMember.getLastName()).build();
    }

    @Override
    public void deleteAll() {
        memberRepository.deleteAll();
    }

    @Override
    public MemberResponse findById(String memberId) throws CooperaException {
      var  foundMember = memberRepository.findById(memberId);
        if (foundMember.isEmpty()) {
            throw new CooperaException("Member with id " + memberId +" not found");
        }
        return MemberResponse.builder().id(foundMember.get().getId()).role(foundMember.get().getRoles()).name(foundMember.get().getFirstName() + " "+ foundMember.get().getLastName()).build();

    }

    @Override
    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    private Member initializeNewMember(RegisterMemberRequest request) {
        Member newMember = new Member();
        newMember.setFirstName(request.getFirstName());
        newMember.setId(request.getMemberId());
        newMember.setLastName(request.getLastName());
        newMember.setEmail(request.getEmail());
        newMember.setBalance(BigDecimal.ZERO);
        newMember.setPassword(passwordEncoder.encode(request.getPassword()));
        newMember.setPosition(request.getPosition());
        newMember.setPhoneNumber(request.getPhoneNumber());
        newMember.getRoles().add(Role.MEMBER);
        return newMember;
    }

    private void checkIfMemberExistByEmail(RegisterMemberRequest request) throws CooperaException {
        Optional<Member> existingMember = memberRepository.findByEmail(request.getEmail());
        if (existingMember.isPresent()) {
            throw new CooperaException("Member with this email already exists");
        }
    }
}
