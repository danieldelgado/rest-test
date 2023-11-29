package com.bci.reactive.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = { "/", "index" })
public class FrontWebController {

	@RequestMapping("/")
	public String greet() {
		return "index.html";
	}

}
