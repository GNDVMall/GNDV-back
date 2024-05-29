package com.gndv.member.domain.entity;

import com.gndv.constant.Role;
import com.gndv.constant.Status;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member implements Serializable {

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
}
