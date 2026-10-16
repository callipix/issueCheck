package com.project.exam.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {

	private final JavaMailSender mailSender;

	@Value("${spring.mail.username}")
	private String from;

	public void sendVerificationMail(String email, String token) throws MessagingException {
		String verificationUrl = "http://localhost:8080/auth/signUp/verify?token=" + token;

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

		helper.setFrom(from);
		helper.setTo(email);
		helper.setSubject("[IssueCheck] 이메일 인증");
		String html = """
			<h2>[IssueCheck] 이메일 인증</h2>
			<p>아래 링크를 클릭하여 이메일 인증을 완료해 주세요.</p>
			<a href="%s">이메일 인증하기</a>
			<p>인증 링크는 20분 동안 유효합니다.</p>
			""".formatted(verificationUrl);
		helper.setText(html, true);

		mailSender.send(message);
	}

}
