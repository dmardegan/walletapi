package com.wallet.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class UserDTO {

	private Long id;
	@NotNull
	@Length(min=6, message = "The password must have at least 6 characters")
	private String password;
	@Length(min = 3, max = 50, message = "The name must have have between 3 and 50 characters")
	private String name;
	@Email(message="Invalid e-mail")
	private String email;
}
