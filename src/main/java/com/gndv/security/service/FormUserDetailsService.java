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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("userDetailsService")
@RequiredArgsConstructor
public class FormUserDetailsService implements UserDetailsService {

    private final MemberMapper memberMapper;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<Member> memberOptional = memberMapper.findByEmail(email);
        if (memberOptional.isEmpty()) {
            throw new UsernameNotFoundException("No user found with username: " + email);
        }

        Member member = memberOptional.get();

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(member.getRole().toString()));

        ModelMapper mapper = new ModelMapper();
        MemberDTO memberDTO = mapper.map(member, MemberDTO.class);

        return new MemberContext(memberDTO, authorities);
    }
}
