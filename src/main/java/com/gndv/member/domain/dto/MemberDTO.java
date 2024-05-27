package com.gndv.member.domain.dto;

import com.gndv.constant.Boolean;
import com.gndv.constant.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {

    private Long member_id;
    private String email;
    private String password;
    private String nickname;
    private String phone;
    private String introduction;
    private LocalDateTime created_at;
    private Long rating;
    private String roles;
    private Long report_count;
    private Status member_status;
    private LocalDateTime last_login;

    // UserDetails
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;
}
