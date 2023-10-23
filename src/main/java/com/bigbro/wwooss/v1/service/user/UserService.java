package com.bigbro.wwooss.v1.service.user;

import com.bigbro.wwooss.v1.domain.response.auth.UserResponse;

public interface UserService {

    /**
     *
     * @return 로그인 한 유저의 정보
     */
    UserResponse getUserInfo();
}
