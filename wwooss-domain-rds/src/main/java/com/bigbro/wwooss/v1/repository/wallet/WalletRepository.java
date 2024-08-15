package com.bigbro.wwooss.v1.repository.wallet;

import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.entity.wallet.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByUser(User user);
}
