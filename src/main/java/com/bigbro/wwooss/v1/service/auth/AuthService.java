package com.bigbro.wwooss.v1.service.auth;

import com.bigbro.wwooss.v1.domain.request.auth.UserExistsRequest;
import com.bigbro.wwooss.v1.domain.request.auth.UserLoginRequest;
import com.bigbro.wwooss.v1.domain.request.auth.UserRegistrationRequest;
import com.bigbro.wwooss.v1.domain.response.auth.UserResponse;

import javax.servlet.http.HttpServletResponse;

public interface AuthService {
     UserResponse login(UserLoginRequest userLoginRequest, HttpServletResponse response);

     void logout();

     UserResponse register(UserRegistrationRequest userRegistrationRequest, HttpServletResponse response);

     Boolean existsUser(UserExistsRequest userExistsRequest);
}
