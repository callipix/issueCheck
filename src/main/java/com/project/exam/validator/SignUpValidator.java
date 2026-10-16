package com.project.exam.validator;

import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.project.exam.dto.SignUpRequest;

@Component
public class SignUpValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return SignUpRequest.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		SignUpRequest request = (SignUpRequest) target;

		if(!Objects.equals(
			request.getPassword(),
			request.getPasswordConfirm()))
		{
			errors.rejectValue("password", "passwordMismatch", "비밀번호가 일치하지 않습니다.");
		}
	}
}
