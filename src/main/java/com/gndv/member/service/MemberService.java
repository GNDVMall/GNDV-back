package com.gndv.member.service;

import com.gndv.member.domain.dto.InsertFormDTO;
import com.gndv.member.domain.dto.UpdateFormDTO;
import com.gndv.member.domain.entity.Member;
import com.gndv.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberMapper memberMapper;

    @Transactional
    public Member insertMember(Member member) {
       return memberMapper.insertMember(member);
    }

    @Transactional
    public void updateMember(UpdateFormDTO updateForm) {
        memberMapper.updateMember(updateForm);
    }

    public Member selectMemberById(Long member_id) {
        return memberMapper.selectMemberById(member_id);
    }

    public Member selectMemberByEmail(String email) {
        return memberMapper.selectMemberByEmail(email);
    }

    public List<Member> selectAllMember() {
        return memberMapper.selectAllMember();
    }

    @Transactional
    public void deleteMemberById(Long member_id) {
        memberMapper.deleteMemberById(member_id);
    }
}
