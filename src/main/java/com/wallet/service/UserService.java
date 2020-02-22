package com.wallet.service;

import java.util.Optional;

import com.wallet.entity.Users;

public interface UserService {

	Users save(Users u);

	Optional<Users> findByEmail(String email);
}
