package com.bigbro.wwooss.v1.repository.user;


import com.bigbro.wwooss.v1.domain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
