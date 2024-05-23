package com.gndv.member.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class JoinRequest {

    private String email;
    private String password;
}
