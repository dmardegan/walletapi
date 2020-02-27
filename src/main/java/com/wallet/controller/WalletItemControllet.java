package com.wallet.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wallet.dto.WalletItemDTO;
import com.wallet.entity.Wallet;
import com.wallet.entity.WalletItem;
import com.wallet.response.Response;
import com.wallet.service.WalletItemService;
import com.wallet.util.enums.WalletItemType;

@RestController
@RequestMapping("/wallet-item")
public class WalletItemControllet {

	@Autowired
	private WalletItemService service;
	
	@PostMapping
	public ResponseEntity<Response<WalletItemDTO>> create(@Valid @RequestBody WalletItemDTO dto, BindingResult result) {
		Response<WalletItemDTO> response = new Response<WalletItemDTO>();
		
		if(result.hasErrors()) {
			result.getAllErrors().forEach(e -> response.getErrors().add(e.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		WalletItem wi = service.save(this.convertDTO2Entity(dto));
		response.setData(this.convertEntity2DTO(wi));
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@GetMapping(value = "/{wallet}")
	public ResponseEntity<Response<Page<WalletItemDTO>>> findBetweenDates(@PathVariable("wallet") Long wallet,
			@RequestParam("startDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date startDate,
			@RequestParam("endDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date endDate,
			@RequestParam(name = "page", defaultValue = "0") int page) {
		
		Response<Page<WalletItemDTO>> response = new Response<Page<WalletItemDTO>>();
		Page<WalletItem> items = service.findBetweenDates(wallet, startDate, endDate, page);
		Page<WalletItemDTO> dto = items.map(wi -> this.convertEntity2DTO(wi));
		response.setData(dto);

		return ResponseEntity.ok().body(response);
	}
	
	@GetMapping(value = "/type/{wallet}")
	public ResponseEntity<Response<List<WalletItemDTO>>> findByWalletIdAndType(@PathVariable("wallet") Long wallet,
			@RequestParam("type") String type) {
		
		Response<List<WalletItemDTO>> response = new Response<List<WalletItemDTO>>();
		
		List<WalletItem> listItem = service.findByWalletAndType(wallet, WalletItemType.getEnum(type));
		List<WalletItemDTO> dto = new ArrayList<WalletItemDTO>();

		listItem.forEach(wi -> dto.add(this.convertEntity2DTO(wi)));
		response.setData(dto);
		
		return ResponseEntity.ok().body(response);
	}
	
	@GetMapping(value = "/total/{wallet}")
	public ResponseEntity<Response<BigDecimal>> sumByWalletId(@PathVariable("wallet") Long wallet) {
		
		Response<BigDecimal> response = new Response<BigDecimal>();

		BigDecimal value = service.sumByWalletId(wallet);
		response.setData(value == null ? BigDecimal.ZERO : value);
		
		return ResponseEntity.ok().body(response);
	}
	
	@PutMapping
	public ResponseEntity<Response<WalletItemDTO>> update(@Valid @RequestBody WalletItemDTO dto, BindingResult result) {
		
		Response<WalletItemDTO> response = new Response<WalletItemDTO>();

		Optional<WalletItem> wi = service.findById(dto.getId());
		
		if(! wi.isPresent()) {
			result.addError(new ObjectError("WalletItem", "WalletItem not found"));
		} else if(wi.get().getWallet().getId().compareTo(dto.getWallet()) != 0) {
			result.addError(new ObjectError("WalletItemChanged", "You can't change this wallet"));			
		}
		
		if(result.hasErrors()) {
			result.getAllErrors().forEach(e -> response.getErrors().add(e.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		WalletItem item = service.save(this.convertDTO2Entity(dto));
		response.setData(this.convertEntity2DTO(item));
		
		return ResponseEntity.ok().body(response);
	}
	
	@DeleteMapping
	public ResponseEntity<Response<String>> delete(@PathVariable("wallet") Long wallet) {
		
		Response<String> response = new Response<String>();

		Optional<WalletItem> wi = service.findById(wallet);
		
		if(! wi.isPresent()) {
			response.getErrors().add("WalletItem not found: id " + wallet);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		
		service.deleteById(wallet);
		
		response.setData("The wallet " + wallet + " was deleted successful");
		
		return ResponseEntity.ok().body(response);
	}
	
	private WalletItem convertDTO2Entity(WalletItemDTO dto) {
		WalletItem e = new WalletItem();
		e.setDate(dto.getDate());
		e.setDescription(dto.getDescription());
		e.setId(dto.getId());
		e.setType(WalletItemType.getEnum(dto.getType()));
		e.setValue(dto.getValue());
		
		Wallet w = new Wallet();
		w.setId(dto.getWallet());
		e.setWallet(w);
		
		return e;
	}
	
	private WalletItemDTO convertEntity2DTO(WalletItem e) {
		WalletItemDTO dto = new WalletItemDTO();
		dto.setDate(e.getDate());
		dto.setDescription(e.getDescription());
		dto.setId(e.getId());
		dto.setType(e.getType().getValue());
		dto.setValue(e.getValue());
		dto.setWallet(e.getWallet().getId());
		
		return dto;
	}

}
