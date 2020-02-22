package com.wallet.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.wallet.entity.Users;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class UserRepositoryTest {

	@Autowired
	UserRepository repository;

	private static final String EMAIL = "email@test.com";

	@BeforeAll
	public void setUp() {
		Users u = new Users();
		u.setName("set up user");
		u.setPassword("senha123");
		u.setEmail(UserRepositoryTest.EMAIL);
		repository.save(u);
	}

	@AfterAll
	public void tearDown() {
		repository.deleteAll();
	}

	@Test
	public void findByEmail() {
		Optional<Users> response = repository.findByEmailEquals(UserRepositoryTest.EMAIL);
		assertTrue(response.isPresent());
		assertEquals(response.get().getEmail(), UserRepositoryTest.EMAIL);
	}

	@Test
	public void testSave() {
		Users u = new Users();
		u.setName("teste");
		u.setPassword("12345");
		u.setEmail("teste@test.com.br");

		Users response = repository.save(u);
		assertNotNull(response);
	}
}
