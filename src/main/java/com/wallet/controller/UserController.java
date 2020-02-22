package com.wallet.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wallet.dto.UserDTO;
import com.wallet.entity.Users;
import com.wallet.response.Response;
import com.wallet.service.UserService;
import com.wallet.util.Bcrypt;

@RestController
@RequestMapping("User")
public class UserController {

	@Autowired
	private UserService service;

	@PostMapping
	public ResponseEntity<Response<UserDTO>> create(@Valid @RequestBody UserDTO userDTO, BindingResult result) {
		Response<UserDTO> response = new Response<UserDTO>();
		
		if(result.hasErrors()) {
			result.getAllErrors().forEach(e -> response.getErrors().add(e.getDefaultMessage()));
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		
		Users user = service.save(convertDTO2Entity(userDTO));

		response.setData(convertEntity2DTO(user));
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	private Users convertDTO2Entity(UserDTO dto) {
		Users u = new Users();
		u.setId(dto.getId());
		u.setEmail(dto.getEmail());
		u.setName(dto.getName());
		u.setPassword(Bcrypt.getHash(dto.getPassword()));
		return u;
	}

	private UserDTO convertEntity2DTO(Users user) {
		UserDTO dto = new UserDTO();
		dto.setId(user.getId());
		dto.setEmail(user.getEmail());
		dto.setName(user.getName());
		//dto.setPassword(user.getPassword()); //do not show password on return object
		return dto;
	}
}
