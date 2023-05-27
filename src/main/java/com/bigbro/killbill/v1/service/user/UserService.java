package com.bigbro.killbill.v1.service.user;

import com.bigbro.killbill.v1.domain.response.user.GetUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    public GetUserResponse test() {
        return GetUserResponse.of(1L, "YSW");
    }
}
