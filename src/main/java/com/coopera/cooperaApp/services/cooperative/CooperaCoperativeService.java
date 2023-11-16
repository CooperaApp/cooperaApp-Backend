package com.coopera.cooperaApp.services.cooperative;

import com.coopera.cooperaApp.dtos.requests.RegisterCooperativeRequest;
import com.coopera.cooperaApp.dtos.response.RegisterCooperativeResponse;
import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.models.Cooperative;
import com.coopera.cooperaApp.repositories.CooperativeRepository;
import com.coopera.cooperaApp.services.member.MemberService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Slf4j
public class CooperaCoperativeService implements CooperativeService {
    private final CooperativeRepository cooperativeRepository;
    private final MemberService memberService;

    public RegisterCooperativeResponse registerCooperative(RegisterCooperativeRequest request) throws CooperaException {

        Cooperative cooperative = new Cooperative();
        cooperative.setName(request.getCooperativeName());
        cooperative.setLogo(request.getLogo());
        // Do we need to explicitly set this? since the initial will always be Zero
        cooperative.setNumberOfMember(cooperative.getMembersId().size());
        cooperative.setDateCreated(LocalDateTime.now());
       Cooperative savedCooperative = cooperativeRepository.save(cooperative);
        if(savedCooperative.getId() == null) throw new CooperaException("Cooperative registration failed");
        return RegisterCooperativeResponse.builder().id(savedCooperative.getId()).name(savedCooperative.getName()).build();
    }

    public RegisterCooperativeResponse addMemberToCooperative (String id, String memberId) throws CooperaException {
        var foundCooperative  = cooperativeRepository.findById(id);
        foundCooperative.orElseThrow(()->new CooperaException("Could not find cooperative with " + id ));
        if (memberService.findById(memberId).getId() != null) {
            foundCooperative.get().getMembersId().add(memberId);
            foundCooperative.get().setNumberOfMember(foundCooperative.get().getMembersId().size());
        }
        Cooperative savedCooperative = cooperativeRepository.save(foundCooperative.get());
        return RegisterCooperativeResponse.builder().id(savedCooperative.getId()).numberOfMembers(savedCooperative.getNumberOfMember()).name(savedCooperative.getName()).build();
    }

    @Override
    public void deleteAll() {
        cooperativeRepository.deleteAll();
    }
}
