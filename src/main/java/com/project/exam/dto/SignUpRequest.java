package com.project.exam.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {

	@NotBlank(message = "아이디를 입력해 주세요.")
	@Pattern(
		regexp = "^(?=.*[a-zA-Z])[a-zA-Z0-9]{8,12}$",
		message ="아이디는 영문을 포함한 8~12자의 영문●숫자만 사용할 수 있습니다."
	)
	private String loginId;

	@NotBlank(message = "비밀번호를 입력해주세요.")
	@Pattern(
		regexp = "^[!-~]{12,64}$",
		message = "비밀번호는 공백없이 영문●숫자●특수문자로 구성된 12~64자여야 합니다."
	)
	private String password;

	@NotBlank(message = "비밀번호 확인을 입력해주세요.")
	private String passwordConfirm;

	@NotBlank(message = "닉네임을 입력해주세요.")
	@Size(min = 2, max = 20, message = "닉네임은 2~20자여야 합니다.")
	@Pattern(
		regexp = "^[가-힣a-zA-Z0-9]+$",
		message = "닉네임은 한글, 영문, 숫자만 사용가능합니다."
	)
	private String nickname;

	@NotBlank(message = "이메일을 입력해 주세요.")
	@Email(message = "올바른 이메일 형식이 아닙니다.")
	@Size(max = 255)
	private String email;

}
