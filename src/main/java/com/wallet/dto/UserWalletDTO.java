package com.wallet.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UserWalletDTO {

	private Long id;
	@NotNull(message = "Inform the user id")
	private Long user;
	@NotNull(message = "Inform the wallet id")
	private Long wallet;
}
