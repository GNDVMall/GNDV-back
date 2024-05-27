package com.gndv.member.domain.entity;

import com.gndv.constant.Boolean;
import com.gndv.constant.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Member implements Serializable {

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
