package com.project.exam.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MailConfig {

	@Value("${spring.mail.username}")
	private String username;

	@Value("${spring.mail.password}")
	private String password;

	@PostConstruct
	public void check()
	{
		log.info("메일 계정: {}" ,username);
		log.info("비밀번호 설정여부: {}", (password != null && !password.isBlank()));
	}
}
