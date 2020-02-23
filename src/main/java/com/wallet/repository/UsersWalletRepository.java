package com.wallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wallet.dto.UsersWallet;

public interface UsersWalletRepository extends JpaRepository<UsersWallet, Long> {
	
}
