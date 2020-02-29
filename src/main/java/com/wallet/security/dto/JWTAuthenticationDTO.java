package com.wallet.security.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class JWTAuthenticationDTO {

	@NotNull(message = "The email can't be null")
	@NotEmpty(message = "The email can't be blank")
	private String email;
	
	@NotNull(message = "The password can't be null")
	@NotEmpty(message = "The password can't be blank")
	private String password;
}
