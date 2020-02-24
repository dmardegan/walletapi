package com.wallet.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.wallet.entity.Wallet;

import lombok.Data;

@Data
public class WalletItemDTO {

	private Long id;
	@NotNull(message = "Inform the related wallet")
	private Wallet wallet;
	@NotNull(message = "Inform the walletitem date")
	private Date date;
	@NotNull(message = "Inform the walletitem type")
	@Pattern(regexp = "^(INBOUD|OUTBOUND)$", message = "For the 'type' are accepted only 'INBOUND'or 'OUTBOUND' values")
	private String type;
	@NotNull(message = "Inform the walletitem description")
	@Length(min = 5, max = 500, message = "The message must have at least 5 characters and max 500")
	private String description;
	@NotNull(message = "Inform the walletitem value")
	private BigDecimal value;
	
}
