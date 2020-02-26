package com.wallet.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.wallet.entity.Wallet;
import com.wallet.entity.WalletItem;
import com.wallet.util.enums.WalletItemType;

@ExtendWith (SpringExtension.class)
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
public class WalletItemRepositoryTest {
	
	//TODO: refactoring of constaints
	private static final Date DATE = new Date();
	private static final WalletItemType TYPE = WalletItemType.IN;
	private static final String DESCRIPTION = "Conta de Luz";
	private static final BigDecimal VALUE = BigDecimal.valueOf(255);
	
	private Long savedWalletItem;
	private Long savedWallet;
	
	
	@Autowired
	WalletItemRepository repository;
	
	@Autowired
	WalletRepository walletRepository;
	
	@BeforeAll
	public void setUp() {
		Wallet w = new Wallet();
		w.setName("testWallet1000");
		w.setValue(BigDecimal.valueOf(1000));
		
		//saving the new wallet
		w = walletRepository.save(w);
		
		//the wallet can't not be null
		assertNotNull(w);
		
		//set up the created wallet - all future test will use them
		savedWallet = w.getId();
		
		WalletItem wi = new WalletItem(0L, w, DATE, TYPE, DESCRIPTION, VALUE);
		//saving the new wallet item
		WalletItem response = repository.save(wi);
		assertNotNull(response);
		assertEquals(response.getDate(), DATE);
		assertEquals(response.getDescription(), DESCRIPTION);
		assertEquals(response.getType(), TYPE);
		assertEquals(response.getValue(), VALUE);
		
		//set up the created wallet item - all future test will use them
		savedWalletItem = response.getId();		
	}

	@AfterAll
	public void tearDown() {
		repository.deleteAll();
		walletRepository.deleteAll();
	}
	
	@Test
	@Order(1)
	public void testSave() {
		Wallet w = new Wallet();
		w.setName("tWallet1");
		w.setValue(BigDecimal.valueOf(1000));
		
		//saving the new wallet
		w = walletRepository.save(w);
		
		//the wallet can't not be null
		assertNotNull(w);
		
		WalletItem wi = new WalletItem(0L, w, DATE, TYPE, DESCRIPTION, VALUE);
		//saving the new wallet item
		WalletItem response = repository.save(wi);
		assertNotNull(response);
		assertEquals(response.getDate(), DATE);
		assertEquals(response.getDescription(), DESCRIPTION);
		assertEquals(response.getType(), TYPE);
		assertEquals(response.getValue(), VALUE);
	}

	//@Test(expected = ConstraintViolation.class)
	@Test
	@Order(2)
	public void testSaveInvalidWalletItem() {
		WalletItem wi = new WalletItem(0L, null, DATE, TYPE, null, VALUE);
		
		Assertions.assertThrows(ConstraintViolationException.class, () -> repository.save(wi));
	}

	@Test
	@Order(95)
	public void testFindByTypeOut() {
		Optional<Wallet> w = walletRepository.findById(savedWallet);
		repository.save(new WalletItem(null, w.get(), DATE, WalletItemType.OU, DESCRIPTION, VALUE));
		
		List<WalletItem> response = repository.findByWalletIdAndType(savedWallet, WalletItemType.OU);
		assertEquals(response.size(), 1);
		assertEquals(response.get(0).getType(), WalletItemType.OU);
	}
	
	@Test
	@Order(96)
	public void testFindByType() {
		List<WalletItem> response = repository.findByWalletIdAndType(savedWallet, TYPE);
		assertEquals(response.size(), 1);
		assertEquals(response.get(0).getType(), TYPE);
	}
	
	@Test
	@Order(97)
	public void testFindBetweenDates() {
		Optional<Wallet> w = walletRepository.findById(savedWallet);
		
		LocalDateTime localDateTime = DATE.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		
		Date currentDatePlusFiveDays = Date.from(localDateTime.plusDays(5).atZone(ZoneId.systemDefault()).toInstant());
		Date currentDatePlusSevenDays = Date.from(localDateTime.plusDays(7).atZone(ZoneId.systemDefault()).toInstant());
		
		repository.save(new WalletItem(null, w.get(), currentDatePlusFiveDays, TYPE, DESCRIPTION, VALUE));
		repository.save(new WalletItem(null, w.get(), currentDatePlusSevenDays, TYPE, DESCRIPTION, VALUE));
		
		PageRequest pg = PageRequest.of(0, 10);
		Page<WalletItem> response = repository.findAllByWalletIdAndDateGreaterThanEqualAndDateLessThanEqual(savedWallet, DATE, currentDatePlusSevenDays, pg);
		
		assertEquals(response.getContent().size(), 2);
		assertEquals(response.getTotalElements(), 2);
		assertEquals(response.getContent().get(0).getWallet().getId(), savedWallet);
	}

	@Test
	@Order(98)
	public void testSumByWallet() {
		System.out.println("##############testSumByWallet");
		Optional<Wallet> w = walletRepository.findById(savedWallet);
		repository.save(new WalletItem(null, w.get(), DATE, WalletItemType.OU, DESCRIPTION, VALUE));
		
		
		BigDecimal response = repository.sumByWalletId(savedWallet);
		System.out.println("######### w: " + savedWallet + " - $ " + response.toString());
		assertEquals(response.compareTo(BigDecimal.valueOf(1275)), 0);
	}
	
	@Test
	@Order(99)
	public void testUpdate() {
		Optional<WalletItem> wi = repository.findById(savedWalletItem);
		WalletItem walletItemChanged = wi.get();
		
		String newDescription = "Wallet Item '" + walletItemChanged.getDescription() + "' changed";
		walletItemChanged.setDescription(newDescription);
		repository.save(walletItemChanged);
		
		Optional<WalletItem> newWalletItem = repository.findById(savedWalletItem);
		assertEquals(newDescription, newWalletItem.get().getDescription());
		
	}
	
	@Test
	@Order(100)
	@DisplayName("test delete wallet item by id")
	public void testDeleteWalletItem() {
		Optional<WalletItem> wi = repository.findById(savedWalletItem);
		//tests if find the wallet item
		assertTrue(wi.isPresent());
		
		//delete the wallet item
		WalletItem walletItemDelete = wi.get();
		repository.deleteById(walletItemDelete.getId());
		
		//search the deleted wallet item
		wi = repository.findById(savedWalletItem);
		
		//tests if the wallet item is not present
		assertFalse(wi.isPresent());
	}
}
