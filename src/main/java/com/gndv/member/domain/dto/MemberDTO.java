package com.gndv.member.domain.dto;

import com.gndv.constant.Role;
import com.gndv.constant.Status;
import com.gndv.member.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

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
    private Date created_at;
    private Long rating;
    private Long report_count;
    private Status member_status;
    private Date last_login;

    // Enum
    private Role role;

    // UserDetails
    private boolean is_account_non_expired;
    private boolean is_account_non_locked;
    private boolean is_credentials_non_expired;
    private boolean is_enabled;

    // Jwt
    private String refreshToken;

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void destroyRefreshToken() {
        this.refreshToken = null;
    }

}
