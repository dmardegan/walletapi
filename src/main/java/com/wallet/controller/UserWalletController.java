package com.wallet.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wallet.dto.UserWalletDTO;
import com.wallet.entity.User;
import com.wallet.entity.UserWallet;
import com.wallet.entity.Wallet;
import com.wallet.response.Response;
import com.wallet.service.UserWalletService;

@RestController
@RequestMapping("user-wallet")
public class UserWalletController {

	@Autowired
	UserWalletService service;
	
	@PostMapping
	public ResponseEntity<Response<UserWalletDTO>> create(@Valid @RequestBody UserWalletDTO userWalletDTO, BindingResult result) {
		Response<UserWalletDTO> response = new Response<UserWalletDTO>();
		
		if(result.hasErrors()) {
			result.getAllErrors().forEach(e -> response.getErrors().add(e.getDefaultMessage()));
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		
		UserWallet userWallet = service.save(convertDTO2Entity(userWalletDTO));

		response.setData(convertEntity2DTO(userWallet));
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	
	private UserWallet convertDTO2Entity(UserWalletDTO dto) {
		UserWallet uw = new UserWallet();
		
		User u = new User();
		u.setId(dto.getUser());
		uw.setUser(u);
		
		Wallet w = new Wallet();
		w.setId(dto.getWallet());
		uw.setWallet(w);
		
		return uw;
	}

	private UserWalletDTO convertEntity2DTO(UserWallet uw) {
		UserWalletDTO dto = new UserWalletDTO();
		dto.setId(uw.getId());
		dto.setUser(uw.getUser().getId());
		dto.setWallet(uw.getWallet().getId());
		return dto;
	}

}
