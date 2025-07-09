package generator.controller;

import generator.domain.User;
import generator.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
public class UserController {
	@Autowired
	private UserService userService;
	@RequestMapping("/public/register")
	public String register(@RequestBody User user) {
		// 1. 获取Keycloak管理员Token
		String adminToken = getAdminToken();

		// 2. 创建用户
		String userId = createUserInKeycloak(user.getUser_name(), user.getUser_password(), adminToken);

		// 3. 分配user角色
		assignUserRole(userId, adminToken);

		userService.save(user);

		return "注册成功";
	}

	@RequestMapping("/user/hello")
	public String hello2() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		System.out.println("当前用户: " + authentication.getName());
		System.out.println("拥有的权限: " + authentication.getAuthorities());
		return "hello";
	}
	// 获取Keycloak管理员Token
	private String getAdminToken() {
		RestTemplate restTemplate = new RestTemplate();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("client_id", "admin-cli");
		params.add("username", "xby"); // Keycloak管理员用户名
		params.add("password", "1234"); // Keycloak管理员密码
		params.add("grant_type", "password");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

		ResponseEntity<Map> response = restTemplate.postForEntity(
				"http://localhost:8080/auth/realms/master/protocol/openid-connect/token",
				request, Map.class);

		return (String) response.getBody().get("access_token");
	}

	// 创建用户
	private String createUserInKeycloak(String username, String password, String adminToken) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(adminToken);

		Map<String, Object> user = new HashMap<>();
		user.put("username", username);
		user.put("enabled", true);

		Map<String, Object> cred = new HashMap<>();
		cred.put("type", "password");
		cred.put("value", password);
		cred.put("temporary", false);

		user.put("credentials", Collections.singletonList(cred));

		HttpEntity<Map<String, Object>> request = new HttpEntity<>(user, headers);

		ResponseEntity<String> response = restTemplate.postForEntity(
				"http://localhost:8080/auth/admin/realms/VideoApp/users",
				request, String.class);

		// 获取新用户ID（从Location头获取）
		String location = response.getHeaders().getFirst("Location");
		if (location != null) {
			return location.substring(location.lastIndexOf('/') + 1);
		}
		throw new RuntimeException("注册用户失败");
	}

	// 分配user角色
	private void assignUserRole(String userId, String adminToken) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(adminToken);

		// 获取user角色信息
		ResponseEntity<Map> roleResp = restTemplate.exchange(
				"http://localhost:8080/auth/admin/realms/VideoApp/roles/user",
				HttpMethod.GET,
				new HttpEntity<>(headers),
				Map.class
		);
		Map<String, Object> role = roleResp.getBody();
		System.out.println("role: " + role);

		// 只保留id和name字段
		Map<String, Object> simpleRole = new HashMap<>();
		simpleRole.put("id", role.get("id"));
		simpleRole.put("name", role.get("name"));
		List<Map<String, Object>> roles = new ArrayList<>();
		roles.add(simpleRole);
		System.out.println("roles: " + roles);

		HttpEntity<List<Map<String, Object>>> request = new HttpEntity<>(roles, headers);
		ResponseEntity<String> resp = restTemplate.postForEntity(
				"http://localhost:8080/auth/admin/realms/VideoApp/users/" + userId + "/role-mappings/realm",
				request, String.class);
		System.out.println("assign role response: " + resp);
	}
}
