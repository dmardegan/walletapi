package com.wallet.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.wallet.entity.WalletItem;
import com.wallet.util.enums.WalletItemType;

public interface WalletItemRepository extends JpaRepository<WalletItem, Long> {
	
	Page<WalletItem> findAllByWalletIdAndDateGreaterThanEqualAndDateLessThanEqual(Long wallet, Date init, Date end, Pageable p);
	//Page<WalletItem> findAllByWalletIdAndDateGreaterThanEqual(Long wallet, Date init, Pageable p);
	
	List<WalletItem> findByWalletIdAndType(Long wallet, WalletItemType type);

}
