package com.wallet.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class WalletDTO {

	private Long id;
	@Length(min = 3, message = "The name must have been at least 3 characters")
	private String name;
	@NotNull (message = "The value of wallet can't not be null")
	private BigDecimal value;
	
}
