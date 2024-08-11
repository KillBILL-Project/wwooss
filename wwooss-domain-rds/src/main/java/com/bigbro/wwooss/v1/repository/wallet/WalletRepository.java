package com.bigbro.wwooss.v1.repository.wallet;

import com.bigbro.wwooss.v1.entity.wallet.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Integer> {
}
