package com.gndv.member.domain.dto;

import com.gndv.constant.Boolean;
import com.gndv.constant.Status;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class MemberDTO {

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
