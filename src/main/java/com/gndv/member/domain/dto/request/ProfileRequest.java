package com.gndv.member.domain.dto.request;

import com.gndv.member.domain.entity.Member;
import com.gndv.review.domain.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileRequest {

    private Member member;
    private List<Review> reviews;
}
