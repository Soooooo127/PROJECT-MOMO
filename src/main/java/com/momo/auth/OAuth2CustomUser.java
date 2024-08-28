package com.momo.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class OAuth2CustomUser implements OAuth2User {
	
	private final OAuth2Response oAuth2Response;
	private final String role;
	
	public OAuth2CustomUser(OAuth2Response oAuth2Response, String role) {
		this.oAuth2Response = oAuth2Response;
		this.role = role;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return null;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collection = new ArrayList<>();
		collection.add(new GrantedAuthority() {
			
			@Override
			public String getAuthority() {
				return role;
			}
		});
		
		return collection;
	}

	@Override
	public String getName() {
		return oAuth2Response.getName();
	}
	
	
	public String getMemberid() {
		return oAuth2Response.getProvider() + "" + oAuth2Response.getProviderId();
	}
	

}
