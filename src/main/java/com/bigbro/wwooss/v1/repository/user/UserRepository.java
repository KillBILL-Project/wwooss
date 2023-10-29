package com.bigbro.wwooss.v1.repository.user;


import com.bigbro.wwooss.v1.domain.entity.user.User;
import com.bigbro.wwooss.v1.enumType.LoginType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmailAndLoginType(String email, LoginType loginType);
}
