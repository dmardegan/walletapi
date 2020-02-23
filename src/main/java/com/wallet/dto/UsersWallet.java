package com.wallet.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UsersWallet {

	private Long id;
	@NotNull(message = "Inform the user id")
	private Long users;
	@NotNull(message = "Inform the wallet id")
	private Long wallet;
}
