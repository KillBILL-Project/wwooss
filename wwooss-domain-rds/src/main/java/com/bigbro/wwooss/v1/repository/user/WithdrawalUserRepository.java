package com.bigbro.wwooss.v1.repository.user;

import com.bigbro.wwooss.v1.entity.user.WithdrawalUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithdrawalUserRepository extends JpaRepository<WithdrawalUser, Long> {
}
