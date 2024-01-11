package com.bigbro.wwooss.v1.dto.request.user;

import com.bigbro.wwooss.v1.enumType.UserRole;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserCredential {
    Long userId;
    String userEmail;
    UserRole userRole;

    public static UserCredential of(Long userId, String userEmail, UserRole userRole) {
        return UserCredential.builder()
                .userId(userId)
                .userEmail(userEmail)
                .userRole(userRole)
                .build();

    }
}
