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

import com.wallet.dto.UserDTO;
import com.wallet.entity.User;
import com.wallet.response.Response;
import com.wallet.service.UserService;

@RestController
@RequestMapping("User")
public class UserController {

	@Autowired
	private UserService service;

	@PostMapping
	public ResponseEntity<Response<UserDTO>> create(@Valid @RequestBody UserDTO userDTO, BindingResult result) {
		Response<UserDTO> response = new Response<UserDTO>();
		User user = service.save(convertDTO2Entity(userDTO));

		response.setData(convertEntity2DTO(user));
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	private User convertDTO2Entity(UserDTO dto) {
		User u = new User();
		u.setEmail(dto.getEmail());
		u.setName(dto.getName());
		u.setPassword(dto.getPassword());
		return u;
	}

	private UserDTO convertEntity2DTO(User user) {
		UserDTO dto = new UserDTO();
		dto.setEmail(user.getEmail());
		dto.setName(user.getName());
		dto.setPassword(user.getPassword());
		return dto;
	}
}
