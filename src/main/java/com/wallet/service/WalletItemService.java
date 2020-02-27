package com.wallet.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.wallet.entity.WalletItem;
import com.wallet.util.enums.WalletItemType;

public interface WalletItemService {

	WalletItem save(WalletItem i);
	
	Page<WalletItem> findBetweenDates(Long wallet, Date init, Date end, int pageIndex);
	
	List<WalletItem> findByWalletAndType(Long wallet, WalletItemType type);
	
	BigDecimal sumByWalletId(Long wallet);
	
	Optional<WalletItem> findById(Long id);
	
	void deleteById(Long id);
}
