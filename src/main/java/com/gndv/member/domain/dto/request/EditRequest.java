package com.gndv.member.domain.dto.request;

import lombok.Data;

@Data
public class EditRequest {

    private String email;
    private String password;
    private String nickname;
    private String phone;
    private String introduction;
}
