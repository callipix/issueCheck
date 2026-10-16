package com.project.exam.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.exam.dto.PendingRegistration;
import com.project.exam.dto.SignUpRequest;
import com.project.exam.dto.Users;
import com.project.exam.mapper.PendingRegistrationMapper;
import com.project.exam.mapper.UserMapper;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignUpService {

	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;
	private final PendingRegistrationMapper pendingRegistrationMapper;
	private final MailService mailService;

	public boolean isLoginIdDuplicated(String loginId) {
		return userMapper.existsByLoginId(loginId);
	}

	public boolean isNickNameDuplicated(String nickname) {
		return userMapper.existsByNickName(nickname);
	}

	public boolean isEmailDuplicated(String email) {
		return userMapper.existsByEmail(email);
	}

	public void requestSignUp(SignUpRequest request) throws NoSuchAlgorithmException, MessagingException {
		// 비밀번호 BCrypt 암호화
		String encodedPassword = passwordEncoder.encode(request.getPassword());

		// 이메일 인증 토큰 생성
		SecureRandom random = new SecureRandom();
		byte[] tokenBytes = new byte[32];
		random.nextBytes(tokenBytes);

		String token = Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);

		// 토큰 해시 생성
		String hexString = hashToken(token);

		// 인증 만료시간 설정
		LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(20);
		String passwordHash = passwordEncoder.encode(encodedPassword);

		// PendingRegistration 객체 생성
		PendingRegistration pending = new PendingRegistration();

		pending.setLoginId(request.getLoginId());
		pending.setPasswordHash(passwordHash);
		pending.setNickname(request.getNickname());
		pending.setEmail(request.getEmail());

		pending.setTokenHash(hexString);
		pending.setExpiresAt(expiresAt);

		// Mapper를 통해 DB에 저장
		pendingRegistrationMapper.insert(pending);

		// 인증 이메일 발송
		mailService.sendVerificationMail(request.getEmail(), token);
	}

	public boolean isValidToken(String token) throws NoSuchAlgorithmException {

		log.info("토큰확인 {} ", token);

		if(token == null || token.isEmpty()) {
			return false;
		}
		// 2. SHA-256 해싱
		String hexString = hashToken(token);

		// 3. DB에서 토큰 해시 조회
		PendingRegistration pending = pendingRegistrationMapper.findByTokenHash(hexString);

		if (pending == null) {
			log.info("유효한 토큰이 아닙니다.");
			return false;
		}
		// 4. 토큰 만료 여부 확인
		LocalDateTime now = LocalDateTime.now();
		return pending.getExpiresAt().isAfter(now);
	}
@Transactional
	public void completeSignUp(String token) throws NoSuchAlgorithmException {
		// 1. 토큰 검증
		if(token == null || token.isEmpty()){
			throw new IllegalStateException("유효하지 않은 인증 토큰입니다.");
		}
		String hexString = hashToken(token);
		PendingRegistration pending = pendingRegistrationMapper.findByTokenHash(hexString);

		if(pending == null) {
			throw new IllegalStateException("토큰이 존재하지 않습니다.");
		}
		if(!pending.getExpiresAt().isAfter(LocalDateTime.now())){
			throw new IllegalStateException("토큰 유효시간이 만료되었습니다.");
		}

		Users users = new Users();
		users.setLoginId(pending.getLoginId());
		users.setNickname(pending.getNickname());
		users.setEmail(pending.getEmail());
		users.setPassword(pending.getPasswordHash());

		userMapper.insert(users);
		int deleted = pendingRegistrationMapper.deletePendingRegistration(hexString);

		if(deleted !=1)
		{
			throw new IllegalStateException("임시 회원정보 삭제에 실패했습니다.");
		}
	}

	private String hashToken(String token) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] tokenHash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
		String hexString = HexFormat.of().formatHex(tokenHash);
		return hexString;
	}
}
