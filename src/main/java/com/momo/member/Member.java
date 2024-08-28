package com.momo.member;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.momo.auth.OAuth2Member;
import com.momo.image.Image;
import com.momo.member.profile.Profile;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Member implements UserDetails {
	// UserDetails를 상속받아 인증 객체로 활용
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false)
	private Integer no;
	
	@Column(nullable = false, unique = true)
	private String memberid;
	
	@Column(nullable = false, unique = true)
	private String membernick;
	
	@Column(nullable = false)
	private String membername;
	
	private String password;
	
	@Column(nullable = false, unique = true)
	private String email;
	
	private LocalDateTime createDate;
	
	private String role;
	
	@ManyToMany
	private List<Member> friend;
	
	@OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
	private List<OAuth2Member> oauth2MemberList;
	
	@OneToOne
	@JoinColumn(name = "image_no")
	private Image image;
	
	@OneToOne
	@JoinColumn(name = "profile_no")
	private Profile profile;

	
	/*
	@Builder
	public Member(String memberid, String password, String membername
			, String membernick, String email) {
		
		this.memberid = memberid;
		this.password = password;
		this.membername = membername;
		this.membernick = membernick;
		this.email = email;
    }
    */

	public Member updateEmail(String email) {
	    this.email = email;
        return this;
    }
	
	// 권한 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       // 사용자의 권한이 'user'뿐인 경우 반환하는 코드(List.of는 List를 생성하는 메소드이다)
//        return List.of(new SimpleGrantedAuthority("user"));
        
        List<GrantedAuthority> authorities = new ArrayList<>();
        
		if ("admin".equals(memberid)) {
			authorities.add(new SimpleGrantedAuthority(MemberRole.ADMIN.getValue()));
		} else {
			authorities.add(new SimpleGrantedAuthority(MemberRole.MEMBER.getValue()));
		}
		
		return authorities;
		
    }

    // 회원의 아이디 반환
    @Override
    public String getUsername() {
        return memberid;
    }

    @Override
    public String getPassword() {
        return password;
    }

    // 계정 만료 여부 반환(true = 만료되지 않음)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠금 여부 반환(true = 잠기지 않음)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 패스워드 만료 여부 반환(true = 만료되지 않음)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    // 계정 사용 가능 여부(true = 사용 가능)
    @Override
    public boolean isEnabled() {
        return true;
    }





}
