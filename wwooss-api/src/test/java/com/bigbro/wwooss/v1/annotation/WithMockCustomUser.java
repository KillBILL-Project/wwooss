package com.bigbro.wwooss.v1.annotation;

import com.bigbro.wwooss.v1.enumType.UserRole;
import com.bigbro.wwooss.v1.factory.WithMockCustomUserSecurityContextFactory;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {
    String userId() default "1";

    String userEmail() default "wwoosss@";

    UserRole userRole() default UserRole.USER;

}
