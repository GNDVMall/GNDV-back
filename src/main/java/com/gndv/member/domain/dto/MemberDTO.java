package com.gndv.member.domain.dto;

import com.gndv.constant.Role;
import com.gndv.constant.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {

    // Member
    private Long member_id;
    private String email;
    private String password;
    private String nickname;
    private String phone;
    private String introduction;
    private String profile_url;
    private Date created_at;
    private Long rating;
    private Long report_count;
    private Date last_login;

    // Email Verification
    private String email_verification_token;
    private boolean is_email_verified;

    // Enum
    private Role role;
    private Status member_status;

    // UserDetails
    private boolean is_account_non_expired;
    private boolean is_account_non_locked;
    private boolean is_credentials_non_expired;
    private boolean is_enabled;

    // Jwt
    private String accessToken;
    private String refreshToken;

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void destroyRefreshToken() {
        this.refreshToken = null;
    }

    public void verifyEmail() {
        this.is_email_verified = true;
        this.member_status = Status.ACTIVE;
    }
}
