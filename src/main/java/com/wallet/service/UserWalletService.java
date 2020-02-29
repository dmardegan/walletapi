package com.wallet.service;

import java.util.Optional;

import com.wallet.entity.UserWallet;

public interface UserWalletService {

	UserWallet save(UserWallet uw);
	
	Optional<UserWallet> findByUserIdAndWallet(Long user, Long wallet);
}
