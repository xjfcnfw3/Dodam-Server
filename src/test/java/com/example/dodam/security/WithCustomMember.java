package com.example.dodam.security;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithCustomMemberSecurityContextFactory.class)
public @interface WithCustomMember {

    long id() default 1L;
    String email() default "test@naver.com";
    String password() default "123455678";
    String nickname() default "tester";
    String role() default "ROLE_USER";
}
