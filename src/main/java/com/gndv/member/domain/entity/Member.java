package com.gndv.member.domain.entity;

import com.gndv.constant.Boolean;
import com.gndv.constant.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    private Long member_id;
    private String email;
    private String password;
    private String nickname;
    private String phone;
    private Date create_at;
    private Boolean account_lock;
    private Long report_count;
    private Long rating;
    private Status member_status;
    private Date last_login;
    private String introduction;
}
