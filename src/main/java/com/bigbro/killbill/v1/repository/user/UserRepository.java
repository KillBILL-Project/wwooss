package com.bigbro.killbill.v1.repository.user;


import com.bigbro.killbill.v1.domain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
