package com.wallet.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.wallet.entity.Users;
import com.wallet.repository.UserRepository;

@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UserServiceTest {

	@Autowired
	UserService service;
	
	@MockBean
	UserRepository repository;

	@BeforeAll
	public void setUp() {
		BDDMockito.given(repository.findByEmailEquals(Mockito.anyString())).willReturn(Optional.of(new Users()));
	}
	
	@Test
	public void testByEmail() {
		Optional<Users> user = service.findByEmail("email@test.com");
		assertTrue(user.isPresent());
	}

}
