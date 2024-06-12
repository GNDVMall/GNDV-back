package com.gndv.member.domain.dto.request;

import lombok.Data;

@Data
public class LoginRequest {

    private String email;
    private String password;
}
