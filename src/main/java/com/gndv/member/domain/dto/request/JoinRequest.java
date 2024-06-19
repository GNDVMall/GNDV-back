package com.gndv.member.domain.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinRequest {

    @Email(message = "올바른 이메일 형식이 아닙니다")
    @NotBlank(message = "이메일은 필수 입력 항목입니다")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다")
    @Pattern(regexp = "^(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$", message = "비밀번호는 특수문자를 포함하여 최소 8자 이상이어야 합니다")
    private String password;
}
