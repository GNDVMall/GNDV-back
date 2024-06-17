package com.gndv.member.domain.dto.request;

import com.gndv.common.domain.response.PageResponse;
import com.gndv.member.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileRequest {

    private Member member;
    private PageResponse<ProfileDetailsRequest> reviews;
    private Date lastLogin;
}
