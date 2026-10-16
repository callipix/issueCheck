package com.project.exam.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Users {

	private long id;
	private String loginId;
	private String password;
	private String passwordConfirm;
	private String nickname;
	private String email;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
