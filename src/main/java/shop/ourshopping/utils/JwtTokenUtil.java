package shop.ourshopping.utils;

import java.security.Key;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.validation.Errors;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import shop.ourshopping.dto.MemberDTO;

/*
 * (미구현)
 * JWT 사용 토큰
 *  - AccessToken
 *  - RefreshToken
 * JWT 저장 위치
 *  - localStorage
 *  - cookie
 * JWT 단점 : XSS, CSRF 공격에 취약함
 * XSS(악의적인 js 코드를 피해자 웹 브라우저에서 실행)
 *  - HTTP Only, Secure Cookie를 통하여 방지 가능
 * CSRF(다른 사이트에서 우리 사이트의 API 콜을 요청해 실행하는 공격)
 *  - refreshToken을 HTTP Only, Secure Cookie로 가져오고
 */
public class JwtTokenUtil {

	private static final String SECRET_KEY = "PRIVATE_KEY";

	public static String createToken(String subject, MemberDTO memberDTO, Boolean hasPermission, Errors errors) {
		long ttlMillis = (60 * 60 * 24 * 7);
		if (ttlMillis <= 0) {
			throw new RuntimeException("Expiry time must be greater than Zero : [" + ttlMillis + "] ");
		}
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		byte[] secretKeyBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
		Key signingKey = new SecretKeySpec(secretKeyBytes, signatureAlgorithm.getJcaName());
		JwtBuilder builder = Jwts.builder().setSubject(subject).setIssuer("test").setHeaderParam("typ", "JWT")
				.claim("hasPermission", hasPermission).signWith(signatureAlgorithm, signingKey);

		if (memberDTO != null) {
			builder.claim("access", memberDTO);
		}

		if (errors != null) {
			builder.claim("errors", errors);
		}

		long nowMillis = System.currentTimeMillis();
		builder.setExpiration(new Date(nowMillis + ttlMillis));
		return builder.compact();
	}

	public static String getSubject(String token) {
		Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
				.parseClaimsJws(token).getBody();
		return claims.getSubject();
	}

	public static Claims getTokenData(String token) {
		Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
				.parseClaimsJws(token).getBody();
		return claims;
	}

	public static Map<String, Object> getSubjectMap(String token) {
		String subject = getSubject(token);
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("result", subject);
		return map;
	}
}