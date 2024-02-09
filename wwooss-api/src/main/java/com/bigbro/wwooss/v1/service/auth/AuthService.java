package com.bigbro.wwooss.v1.service.auth;


import com.bigbro.wwooss.v1.dto.request.auth.UserExistsRequest;
import com.bigbro.wwooss.v1.dto.request.auth.UserLoginRequest;
import com.bigbro.wwooss.v1.dto.request.auth.UserRegistrationRequest;
import com.bigbro.wwooss.v1.dto.response.auth.TokenResponse;

public interface AuthService {
     /**
      *
      * @param userLoginRequest 로그인 타입, 이메일, 비밀번호
      * @return 발급한 토큰 정보
      */
     TokenResponse login(UserLoginRequest userLoginRequest);

     /**
      * DB에 저장된 리프레시 토큰 정보 초기화
      */
     void logout();

     /**
      *
      * @param userRegistrationRequest 회웝가입 시 입력한 정보
      * @return 발급한 토큰 정보
      */
     TokenResponse register(UserRegistrationRequest userRegistrationRequest);

     /**
      *
      * @return 발급한 액세스 토큰 정보
      */
     TokenResponse reissue();

     /**
      *
      * @param userExistsRequest 로그인 타입, 이메일
      * @return 로그인 타입이 일치하는 이메일 존재 여부
      */
     Boolean existsUser(UserExistsRequest userExistsRequest);

     /**
      * 회원 탈퇴
      */
     void withdrawalUser(Long userId);

     /**
      * 비밀번호 초기화
      * 입력 받은 이메일에 임의의 값 비밀번호 전송
      */
     void resetPassword(String email);

}
