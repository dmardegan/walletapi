package com.wallet.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wallet.entity.Users;
import com.wallet.repository.UserRepository;
import com.wallet.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository repository;

	@Override
	public Users save(Users u) {
		return repository.save(u);
	}

	@Override
	public Optional<Users> findByEmail(String email) {
		return repository.findByEmailEquals(email);	
	}

}
