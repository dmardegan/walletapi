package com.wallet.security.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.wallet.entity.User;
import com.wallet.security.JWTUserFactory;
import com.wallet.service.UserService;

@Service
public class JWTUserDetaislService implements UserDetailsService {

	@Autowired
	UserService userService;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<User> user = userService.findByEmail(email);
		
		if(user.isPresent()) {
			return JWTUserFactory.create(user.get());
		}
		
		throw new UsernameNotFoundException("Email not found.");
	}

}
