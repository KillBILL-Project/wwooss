package com.bigbro.wwooss.v1.annotation;

import com.bigbro.wwooss.v1.config.TestConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith({RestDocumentationExtension.class, MockitoExtension.class})
@AutoConfigureRestDocs
@ActiveProfiles("test")
@WithMockUser("test user")
@Import({TestConfig.class})
public @interface TestController {
}
