package com.gndv.member.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateFormDTO {

    private String email;
    private String password;
    private String nickname;
    private String phone;
    private String introduction;
}
