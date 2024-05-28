/*
package com.gndv.security.listener;

import com.gndv.admin.mapper.RoleMapper;
import com.gndv.member.domain.entity.Role;
import com.gndv.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false; // 이미 설정이 완료되었는지 여부를 확인하는 플래그
    private final MemberMapper memberMapper;
    private final RoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }
        setupData(); // 초기 데이터를 설정
        alreadySetup = true; // 설정이 완료되었음을 표시
    }

    private void setupData() {
        HashSet<Role> roles = new HashSet<>();
        Role adminRole = createRoleIfNotFound("ROLE_ADMIN", "관리자");
        roles.add(adminRole);
        createUserIfNotFound("admin", "admin@admin.com", "pass", roles);
    }

    public Role createRoleIfNotFound(String role_name, String role_desc) {
        Role role = roleMapper.findByRoleName(role_name); // 역할이 존재하는지 확인

        if (role == null) {
            role = Role.builder()
                    .role_name(role_name)
                    .role_desc(role_desc)
                    .build();
        }
        return roleMapper.save(role); // 역할이 존재하지 않으면 저장
    }

    public void createUserIfNotFound(final String userName, final String email, final String password, Set<Role> roleSet) {
        Account account = memberMapper.findByUsername(userName); // 사용자가 존재하는지 확인

        if (account == null) {
            account = Account.builder()
                    .username(userName)
                    .password(passwordEncoder.encode(password))
                    .userRoles(roleSet)
                    .build();
        }
        memberMapper.save(account); // 사용자가 존재하지 않으면 저장
    }
}
*/
