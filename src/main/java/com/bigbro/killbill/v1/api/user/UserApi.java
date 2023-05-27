package com.bigbro.killbill.v1.api.user;

import com.bigbro.killbill.v1.domain.response.user.GetUserResponse;
import com.bigbro.killbill.v1.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserApi {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<GetUserResponse> test() {
        return new ResponseEntity<GetUserResponse>(userService.test(),HttpStatus.OK);
    }
}
