package com.example.dodam.security;

import com.example.dodam.config.auth.MemberDetails;
import com.example.dodam.domain.member.Member;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithCustomMemberSecurityContextFactory implements WithSecurityContextFactory<WithCustomMember> {

    @Override
    public SecurityContext createSecurityContext(WithCustomMember annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Member member = getMember(annotation);
        MemberDetails memberDetails = MemberDetails.create(member);

        UsernamePasswordAuthenticationToken token = generateAuthenticationToken(memberDetails);
        context.setAuthentication(token);

        return context;
    }

    private UsernamePasswordAuthenticationToken generateAuthenticationToken(MemberDetails memberDetails) {
        return new UsernamePasswordAuthenticationToken(memberDetails,
            memberDetails.getPassword(),
            memberDetails.getAuthorities());
    }

    private Member getMember(WithCustomMember annotation) {
        return Member.builder()
            .id(annotation.id())
            .email(annotation.email())
            .password(annotation.password())
            .nickname(annotation.nickname())
            .role(annotation.role())
            .build();
    }
}
