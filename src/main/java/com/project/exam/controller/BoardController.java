package com.project.exam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board")
public class BoardController {

	@GetMapping("/list")
	public String list()
	{
		System.out.println("BoardController 성공");
		return "board/list";
	}

	@GetMapping
	public int deleteList()
	{
		int result = 0;
		return result;
	}
}
