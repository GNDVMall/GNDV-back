package com.gndv.security.service;

import com.gndv.member.domain.dto.MemberContext;
import com.gndv.member.domain.dto.MemberDTO;
import com.gndv.member.domain.entity.Member;
import com.gndv.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("userDetailsService")
@RequiredArgsConstructor
public class FormUserDetailsService implements UserDetailsService {

    private final MemberMapper memberMapper;
    private final ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Member> optionalMember = memberMapper.findByEmail(email);
        if (optionalMember.isEmpty()) {
            throw new UsernameNotFoundException("No user found with email: " + email);
        }

        Member member = optionalMember.get();

        List<GrantedAuthority> authorities = Arrays.stream(member.getRoles().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        MemberDTO memberDTO = modelMapper.map(member, MemberDTO.class);

        return new MemberContext(memberDTO, authorities);
    }
}
