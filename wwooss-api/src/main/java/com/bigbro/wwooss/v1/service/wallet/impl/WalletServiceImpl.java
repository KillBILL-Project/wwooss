package com.bigbro.wwooss.v1.service.wallet.impl;

import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.entity.wallet.Wallet;
import com.bigbro.wwooss.v1.repository.wallet.WalletRepository;
import com.bigbro.wwooss.v1.service.wallet.WalletService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    @Override
    @Transactional
    public void createWallet(User user) {
        walletRepository.save(Wallet.from(user));
    }
}
