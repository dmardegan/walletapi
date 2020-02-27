package com.wallet.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;
import com.wallet.entity.Wallet;

import lombok.Data;

@Data
public class WalletItemDTO {

	private Long id;
	@NotNull(message = "Inform the related wallet")
	private Long wallet;
	@NotNull(message = "Inform the walletitem date")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", locale = "pt-BR", timezone = "Brazil/East")
	private Date date;
	@NotNull(message = "Inform the walletitem type")
	@Pattern(regexp = "^(INBOUND|OUTBOUND)$", message = "For the 'type' are accepted only 'INBOUND'or 'OUTBOUND' values")
	private String type;
	@NotNull(message = "Inform the walletitem description")
	@Length(min = 5, max = 500, message = "The message must have at least 5 characters and max 500")
	private String description;
	@NotNull(message = "Inform the walletitem value")
	private BigDecimal value;
	
}
