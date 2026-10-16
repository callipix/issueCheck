package com.project.exam.controller;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.exam.dto.SignUpRequest;
import com.project.exam.service.SignUpService;
import com.project.exam.validator.SignUpValidator;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class SignUpController {

	private final SignUpValidator signUpValidator;
	private final SignUpService signUpService;

	@GetMapping("/signUp")
	public String signUpForm(Model model)
	{
		model.addAttribute("signUpRequest", new SignUpRequest());
		return "auth/signUp";
	}

	@PostMapping("/signUp")
	public String signUp(
			@Valid @ModelAttribute("signUpRequest") SignUpRequest request,
			BindingResult bindingResult) throws NoSuchAlgorithmException, MessagingException {

		this.signUpValidator.validate(request, bindingResult);

		// 형식오류 검사
		if(bindingResult.hasErrors())
		{
			return "auth/signUp";
		}

		if(signUpService.isLoginIdDuplicated(request.getLoginId()))
		{
			bindingResult.rejectValue(
				"loginId",
			"duplicated",
		"이미 사용중인 아이디입니다.");
		}

		if(signUpService.isNickNameDuplicated(request.getNickname()))
		{
			bindingResult.rejectValue(
				"nickname",
			"duplicated",
		"이미 사용중인 닉네임입니다.");
		}

		if(signUpService.isEmailDuplicated(request.getEmail()))
		{
			bindingResult.rejectValue(
				"email",
			"duplicated",
		"이미 사용중인 이메일입니다.");
		}

		// 중복확인 오류 검사
		if(bindingResult.hasErrors())
		{
			return "auth/signUp";
		}
		signUpService.requestSignUp(request);

		// 추후 임시 회원가입 저장 및 이메일 인증 처리
		return "redirect:/auth/signUp";
		// return "redirect:/auth/signUp/pending";
	}

	@GetMapping("/signUp/verify")
	public String verify(@RequestParam("token") String token, Model model) throws NoSuchAlgorithmException {

		boolean valid = signUpService.isValidToken(token);
		if(valid)
		{
			model.addAttribute("token", token);
			return "auth/verify";
		} else {
			return "auth/verifyError";
		}
	}

	@PostMapping("/signUp/verify")
	public String completeSignUp(@RequestParam("token") String token) throws NoSuchAlgorithmException {

		try {
			signUpService.completeSignUp(token);
			return "redirect:/auth/signUp";
		} catch (IllegalStateException e) {
			return "auth/verifyError";
		}
	}

	@GetMapping("/check-loginId")
	@ResponseBody
	public Map<String, Boolean> checkLoginId(@RequestParam("loginId") String loginId)
	{
		log.info("Check login id : {}", loginId);
		return Map.of("duplicated", signUpService.isLoginIdDuplicated(loginId));
	}

	@GetMapping("/check-nickname")
	@ResponseBody
	public Map<String, Boolean> checkNickname(@RequestParam("nickname") String nickname)
	{
		log.info("Check nickname : {}", nickname);
		return Map.of("duplicated", signUpService.isNickNameDuplicated(nickname));
	}

	@GetMapping("/check-email")
	@ResponseBody
	public Map<String, Boolean> checkEmail(@RequestParam("email") String email)
	{
		return Map.of("duplicated", signUpService.isEmailDuplicated(email));
	}

}
