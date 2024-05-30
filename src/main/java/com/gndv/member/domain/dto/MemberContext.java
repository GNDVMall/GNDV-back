package com.gndv.member.domain.dto;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Data
public class MemberContext implements UserDetails, Serializable {

    private MemberDTO memberDTO;
    private final List<GrantedAuthority> roles;

    public MemberContext(MemberDTO memberDTO, List<GrantedAuthority> roles) {
        this.memberDTO = memberDTO;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return memberDTO.getEmail();
    }

    @Override
    public String getPassword() {
        return memberDTO.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
