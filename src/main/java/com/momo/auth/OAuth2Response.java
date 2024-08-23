package com.momo.auth;

public interface OAuth2Response {

	// 각 소셜 업체(구글, 네이버 등)에서 보내주는 JSON 데이터를 수집할 메소드
	// 제공자 이름(ex: 구글, 네이버)
	String getProvider();
	
	// 각 업체에서의 사용자 id
	String getProviderID();
	
	// 사용자의 email
	String getEmail();
	
	// 사용자의 이름
	String getName();
	
	
}
