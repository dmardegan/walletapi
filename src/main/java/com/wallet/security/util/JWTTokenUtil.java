package com.wallet.security.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.dialect.identity.GetGeneratedKeysDelegate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTTokenUtil {

	static final String CLAIM_KEY_USERNAME = "sub";
	static final String CLAIM_KEY_ROLE = "role";
	static final String CLAIM_KEY_AUDIENCE = "audience";
	static final String CLAIM_KEY_CREATED = "created";
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private Long expiration;
	
	public String getUsernameFromToken(String token) {
		String username;
		
		Claims claims = getClaimsFromToken(token);
		username = claims.getSubject();
		
		return username;
	}
	
	public Date getExpirationDateFromToken(String token) {
		Date expiration;
		Claims claims = getClaimsFromToken(token);
		expiration = claims.getExpiration();
		
		return expiration;
	}
	
	public boolean validToken(String token) {
		return !expiredToken(token);
	}
	
	public String getToken(UserDetails userDetais) {
		Map<String, Object> claims = new HashMap<>();
		claims.put(CLAIM_KEY_USERNAME, userDetais.getUsername());
		claims.put(CLAIM_KEY_CREATED, new Date());
		
		return generateToken(claims);
	}
	
	private Claims getClaimsFromToken(String token) {
		Claims claims;
		try {
		claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			claims = null;
		}
		return claims;
	}
	
	private Date generateExpirationDate() {
		return new Date(System.currentTimeMillis() + expiration * 1000);
	}
	
	private boolean expiredToken(String token) {
		Date expirationDate = this.getExpirationDateFromToken(token);
		if(expirationDate == null) {
			return false;
		}
		return expirationDate.before(new Date());
	}
	
	private String generateToken(Map<String, Object> claims) {
		return Jwts.builder().setClaims(claims)
				.setExpiration(generateExpirationDate())
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();
	}
}