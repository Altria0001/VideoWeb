package generator.controller;

import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class TokenFetcher {

	public static void main(String[] args) {
		String tokenUri = "http://localhost:8080/auth/realms/VideoApp/protocol/openid-connect/token";
		String clientId = "videoback";
		String clientSecret = "Vo4jwWY6fuc80w2fekw3aprUMgbh7mQd";
		String username = "user3";
		String password = "1234";

		RestTemplate restTemplate = new RestTemplate();

		// 设置请求头
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		// 设置请求参数
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "password");
		body.add("client_id", clientId);
		body.add("client_secret", clientSecret);
		body.add("username", username);
		body.add("password", password);
		// 创建请求实体
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

		// 发送请求
		ResponseEntity<Map> response = restTemplate.exchange(tokenUri, HttpMethod.POST, request, Map.class);

		if (response.getStatusCode() == HttpStatus.OK) {
			Map<String, Object> responseBody = response.getBody();
			String accessToken = (String) responseBody.get("access_token");
			System.out.println("Access Token: " + accessToken);
		} else {
			System.out.println("Failed to get token. Status code: " + response.getStatusCode());
		}
	}
}