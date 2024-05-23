package com.gndv.member.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EditRequest {

    private String email;
    private String password;
    private String nickname;
    private String phone;
    private String introduction;
}
