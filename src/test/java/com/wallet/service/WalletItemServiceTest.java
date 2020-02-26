package com.wallet.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.wallet.entity.Wallet;
import com.wallet.entity.WalletItem;
import com.wallet.repository.WalletItemRepository;
import com.wallet.repository.WalletRepository;
import com.wallet.util.enums.WalletItemType;

@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class WalletItemServiceTest {

	private static final Date DATE = new Date();
	private static final String DESCRIPTION = "WServiceTest - Item";
	private static final WalletItemType TYPE = WalletItemType.OU;
	private static final BigDecimal VALUE = BigDecimal.valueOf(309.45);
	private static Long savedWalletId;
	
	@Autowired
	WalletRepository walletRepository;
	
	@Autowired
	WalletItemService service;
	
	@MockBean
	WalletItemRepository repository;

	@BeforeAll
	public void setUp() {
		Wallet w = new Wallet();
		w.setName("WalletSample");
		w.setValue(BigDecimal.valueOf(4958.23));
		
		w = walletRepository.save(w);
		savedWalletId = w.getId();
	}
	
	@Test
	public void testSave() {
		BDDMockito.given(repository.save(Mockito.any(WalletItem.class))).willReturn(getMockWalletItem());
		
		WalletItem response = service.save(new WalletItem());
		assertNotNull(response);
		assertEquals(response.getDescription(), DESCRIPTION);
		assertEquals(response.getValue().compareTo(VALUE), 0);
	}
	
	@Test
	public void testFindBetweenDates() {
		List<WalletItem> itemList = new ArrayList<WalletItem>();
		itemList.add(getMockWalletItem());
		Page<WalletItem> page = new PageImpl<>(itemList);
		
		BDDMockito.given(repository.findAllByWalletIdAndDateGreaterThanEqualAndDateLessThanEqual(
				Mockito.anyLong(), 
				Mockito.any(Date.class), 
				Mockito.any(Date.class), 
				Mockito.any(PageRequest.class))).
				willReturn(page);
		
		Page<WalletItem> response = service.findBetweenDates(0L, new Date(), new Date(), 1);
		assertNotNull(response);
		assertEquals(response.getContent().size(), 1);
		assertEquals(response.getContent().get(0).getValue().compareTo(VALUE), 0);
	}
	
	@Test
	public void testFindByType() {
		List<WalletItem> itemList = new ArrayList<WalletItem>();
		itemList.add(getMockWalletItem());
		
		BDDMockito.given(repository.findByWalletIdAndType(Mockito.anyLong(), Mockito.any(WalletItemType.class))).
				willReturn(itemList);
		
		List<WalletItem> response = service.findByWalletAndType(0L, WalletItemType.IN);
		
		assertNotNull(response);
		assertEquals(response.size(), 1);
		assertEquals(response.get(0).getValue().compareTo(VALUE), 0);
		assertEquals(response.get(0).getType(), TYPE);
	}
	
	@Test
	public void testSumByWallet() {
		BDDMockito.given(repository.sumByWalletId(Mockito.anyLong())).
				willReturn(VALUE);
		
		BigDecimal response = service.sumByWalletId(1L);
		
		assertNotNull(response);
		assertEquals(response.compareTo(VALUE), 0);
	}
	
	private WalletItem getMockWalletItem() {
		Optional<Wallet> w = walletRepository.findById(savedWalletId);
		
		WalletItem wi = new WalletItem();
		wi.setDate(DATE);
		wi.setDescription(DESCRIPTION);
		wi.setType(TYPE);
		wi.setValue(VALUE);
		wi.setWallet(w.get());
		
		return wi;
	}
	
}
