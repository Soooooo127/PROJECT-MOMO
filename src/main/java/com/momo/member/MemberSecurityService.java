package com.momo.member;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberSecurityService implements UserDetailsService {
	// 스프링 시큐리티에서 사용자 정보를 가져오는 인터페이스
	
	private final MemberRepository memberRepository;
	
	@Override
    public Member loadUserByUsername(String memberid) {
        return memberRepository.findBymemberid(memberid)
                 .orElseThrow(() -> new IllegalArgumentException((memberid)));
    }

	/*
	// 회원의 id(memberid)로 정보를 가져오는 메소드(시큐리티 일반 로그인용, OAuth2 사용을 위해 잠시 주석 처리함)
	@Override
	public UserDetails loadUserByUsername(String memberid) throws UsernameNotFoundException {
		Optional<Member> _member = this.memberRepository.findBymemberid(memberid);
		if (_member.isEmpty()) {
			throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
		}
		Member member = _member.get();
		List<GrantedAuthority> authorities = new ArrayList<>();
		if ("admin".equals(memberid)) {
			authorities.add(new SimpleGrantedAuthority(MemberRole.ADMIN.getValue()));
		} else {
			authorities.add(new SimpleGrantedAuthority(MemberRole.MEMBER.getValue()));
		}
		
		MemberDetail memberDetail = new MemberDetail(member.getMemberid(), member.getPassword(), authorities,
				member.getMembernick());
		return memberDetail;
	}
	
	// 현재 사용자를 리턴하는 메소드(OAuth2 사용을 위해 잠시 주석 처리함_)
    public UserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof MemberDetail) {
            return (MemberDetail) principal;
        }
        else {
            return null;
        }
    }
    */

}
