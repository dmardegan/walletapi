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

import com.wallet.dto.WalletDTO;
import com.wallet.entity.Wallet;
import com.wallet.response.Response;
import com.wallet.service.WalletService;

@RestController
@RequestMapping ("wallet")
public class WalletController {

	@Autowired
	WalletService service;
	
	@PostMapping
	public ResponseEntity<Response<WalletDTO>> create(@Valid @RequestBody WalletDTO walletDTO, BindingResult result) {
		Response<WalletDTO> response = new Response<WalletDTO>();
		
		if(result.hasErrors()) {
			result.getAllErrors().forEach(e -> response.getErrors().add(e.getDefaultMessage()));
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		
		Wallet wallet = service.save(convertDTO2Entity(walletDTO));
		

		response.setData(convertEntity2DTO(wallet));
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	
	private Wallet convertDTO2Entity(WalletDTO dto) {
		Wallet w = new Wallet();
		w.setName(dto.getName());
		w.setValue(dto.getValue());
		return w;
	}

	private WalletDTO convertEntity2DTO(Wallet w) {
		WalletDTO dto = new WalletDTO();
		dto.setId(w.getId());
		dto.setName(w.getName());
		dto.setValue(w.getValue());
		return dto;
	}
}
