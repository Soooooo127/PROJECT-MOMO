package com.momo.member;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

@Getter
public class MemberDetail extends User {
	
//	참고 자료
//	https://velog.io/@sin_0/Spring-Spring-Security-User-%EC%BB%A4%EC%8A%A4%ED%85%80%ED%95%98%EA%B8%B0
	
	private final String membernick;
	private final String role;
	
    public MemberDetail(String username, String password, Collection<? extends GrantedAuthority> authorities, String membernick) {
        super(username, password, authorities);
        this.membernick = membernick;
        this.role = authorities.toString();
    }

}
