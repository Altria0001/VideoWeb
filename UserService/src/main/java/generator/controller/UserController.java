package generator.controller;

import generator.domain.User;
import generator.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	@Autowired
	private UserService userService;
	@RequestMapping("/public/hello")
	public String hello() {
		return "hello";
	}
	@RequestMapping("/public/register")
	public String register(@RequestBody User user) {
		userService.register(user);
		return "register";
	}
	@RequestMapping("/user/hello")
	public String hello2() {
		return "hello";
	}
}
