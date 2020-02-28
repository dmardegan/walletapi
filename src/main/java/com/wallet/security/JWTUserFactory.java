package com.wallet.security;

import com.wallet.entity.User;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class JWTUserFactory {

	public static JWTUser create(User user) {
		return new JWTUser(user.getId(), user.getEmail(), user.getPassword());
	}
}
