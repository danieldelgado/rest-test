package com.bci.reactive.config;

import com.bci.reactive.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import java.util.Map;

import java.util.HashMap;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import com.bci.reactive.service.UserService;

@Slf4j
public class CustomTokenEnhancer implements TokenEnhancer {

	@Autowired
	private UserService userService;

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		User dbUser = userService.findByEmail(authentication.getName());

		log.info("enhance username : {} - {} {}", dbUser.getId(), dbUser.getEmail(), accessToken.getValue());

		dbUser.setKeyAccess(accessToken.getValue());
		userService.updateUser(dbUser);

		Map<String, Object> additionalInfo = new HashMap<>();
		Map<String, Object> user = new HashMap<>();
		user.put("id", dbUser.getId());
		user.put("username", dbUser.getEmail());
		user.put("email", dbUser.getEmail());
		user.put("active", dbUser.getId());
		user.put("created", dbUser.getId());
		additionalInfo.put("user", user);
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);

		return accessToken;
	}
}