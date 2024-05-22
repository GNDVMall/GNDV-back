package com.gndv.member.mapper;

import com.gndv.member.domain.dto.InsertFormDTO;
import com.gndv.member.domain.dto.UpdateFormDTO;
import com.gndv.member.domain.entity.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {

    Member insertMember(Member member);
    void updateMember(UpdateFormDTO updateForm);
    Member selectMemberById(Long id);
    Member selectMemberByEmail(String email);
    List<Member> selectAllMember();
    void deleteMemberById(Long member_id);
}
