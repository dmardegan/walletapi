package com.wallet.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.dto.WalletItemDTO;
import com.wallet.entity.Wallet;
import com.wallet.entity.WalletItem;
import com.wallet.service.WalletItemService;
import com.wallet.util.enums.WalletItemType;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
public class WalletItemControllerTest {

	@MockBean
	WalletItemService service;

	@Autowired
	MockMvc mvc;

	private static final Long ID = 1L;
	private static final Date DATE = new Date();
	private static final LocalDate TODAY = LocalDate.now();
	private static final WalletItemType TYPE = WalletItemType.IN;
	private static final String DESCRIPTION = "Conta de Energia";
	private static final BigDecimal VALUE = BigDecimal.valueOf(30029.09);
	private static final String URL = "/wallet-item";

	@BeforeAll
	public void setUp() {
	}

	public void tearDown() {
	}

	@Test
	public void testSave() throws Exception {
		BDDMockito.given(service.save(Mockito.any(WalletItem.class))).willReturn(getMockWalletItem());

		mvc.perform(MockMvcRequestBuilders.post(URL).content(this.getJsonPayload())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.data.id").value(ID))
				.andExpect(jsonPath("$.data.date").value(TODAY.format(this.getFormat())))
				.andExpect(jsonPath("$.data.description").value(DESCRIPTION))
				.andExpect(jsonPath("$.data.type").value(TYPE.getValue()))
				.andExpect(jsonPath("$.data.value").value(VALUE)).andExpect(jsonPath("$.data.wallet").value(ID));
	}

	@Test
	public void testBeweenDates() throws Exception {
		List<WalletItem> listItem = new ArrayList<WalletItem>();
		listItem.add(getMockWalletItem());
		Page<WalletItem> page = new PageImpl<WalletItem>(listItem);

		BDDMockito.given(service.findBetweenDates(Mockito.anyLong(), Mockito.any(Date.class), Mockito.any(Date.class),
				Mockito.anyInt())).willReturn(page);

		String startDate = TODAY.format(getFormat());
		String endDate = TODAY.plusDays(5).format(getFormat());
		String fullURL = URL + "/1?startDate=" + startDate + "&endDate=" + endDate; //WalletID + startDate + endDate

		mvc.perform(MockMvcRequestBuilders.get(fullURL)
				//.content(this.getJsonPayload())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.content[0].id").value(ID))
				.andExpect(jsonPath("$.data.content[0].date").value(TODAY.format(this.getFormat())))
				.andExpect(jsonPath("$.data.content[0].description").value(DESCRIPTION))
				.andExpect(jsonPath("$.data.content[0].type").value(TYPE.getValue()))
				.andExpect(jsonPath("$.data.content[0].value").value(VALUE))
				.andExpect(jsonPath("$.data.content[0].wallet").value(ID));

	}
	
	@Test
	public void testFindByType() throws Exception {
		List<WalletItem> listItem = new ArrayList<WalletItem>();
		listItem.add(getMockWalletItem());
		
		BDDMockito.given(service.findByWalletAndType(Mockito.anyLong(), Mockito.any(WalletItemType.class))).willReturn(listItem);
		String fullURL = URL + "/type/1?type=INBOUND";

		mvc.perform(MockMvcRequestBuilders.get(fullURL)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.[0].id").value(ID))
				.andExpect(jsonPath("$.data.[0].date").value(TODAY.format(this.getFormat())))
				.andExpect(jsonPath("$.data.[0].description").value(DESCRIPTION))
				.andExpect(jsonPath("$.data.[0].type").value(TYPE.getValue()))
				.andExpect(jsonPath("$.data.[0].value").value(VALUE))
				.andExpect(jsonPath("$.data.[0].wallet").value(ID));
	}
	
	@Test
	public void testBySumWallet() throws Exception {
		BigDecimal value = BigDecimal.valueOf(2373.32);
		
		BDDMockito.given(service.sumByWalletId(Mockito.anyLong())).willReturn(value);
		
		String fullURL = URL + "/total/1";

		mvc.perform(MockMvcRequestBuilders.get(fullURL)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data").value("2373.32"));
	}
	
	private String getJsonPayload() throws JsonProcessingException {
		WalletItemDTO dto = new WalletItemDTO();
		dto.setDate(DATE);
		dto.setDescription(DESCRIPTION);
		dto.setId(ID);
		dto.setType(TYPE.getValue());
		dto.setValue(VALUE);
		dto.setWallet(ID);

		ObjectMapper m = new ObjectMapper();
		return m.writeValueAsString(dto);
	}

	private WalletItem getMockWalletItem() {
		Wallet w = new Wallet();
		w.setId(ID);

		WalletItem wi = new WalletItem();
		wi.setId(ID);
		wi.setDate(DATE);
		wi.setDescription(DESCRIPTION);
		wi.setType(TYPE);
		wi.setValue(VALUE);
		wi.setWallet(w);

		return wi;
	}

	private DateTimeFormatter getFormat() {
		return DateTimeFormatter.ofPattern("dd-MM-YYYY");
	}
}
