package com.project.exam.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PendingRegistration {

	private Long id;

	private String loginId;
	private String passwordHash;
	private String nickname;
	private String email;

	private String tokenHash;
	private LocalDateTime expiresAt;
	private LocalDateTime createdAt;
}
