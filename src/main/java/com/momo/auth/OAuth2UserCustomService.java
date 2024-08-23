package com.momo.auth;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.momo.member.Member;
import com.momo.member.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OAuth2UserCustomService extends DefaultOAuth2UserService {
	
	private final MemberRepository memberRepository;
	
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    	System.out.println("userRequest : " + userRequest);
    	
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("oAuth2User : " + oAuth2User.getAttributes());
        
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        
        if(registrationId.equals("google")) {
        	oAuth2Response = new OAuth2ResponseGoogle(oAuth2User.getAttributes());
        } else {
        	return null;
        }
        
        String role = "ROLE_USER";

        /*나중에 다시 할게요(2024.08.23. 하다가 중단..ㅠㅠ)
        // 동일한 이메일을 가진 회원이 있는지 확인
        Optional<Member> temp1 = memberRepository.findMemberByEmail(oAuth2Response.getEmail());
        System.out.println("동일한 이메일 주소를 가진 회원 존재 여부(존재 시 true 출력) : " + temp1.isPresent());
        
        if(temp1.isEmpty()) {
        	//새로운 객체 생성을 위한 임의의 아이디 생성
        	String memberid = oAuth2Response.getProvider() + "" + oAuth2Response.getProviderID();
        	Optional<Member> temp2 = memberRepository.findBymemberid(memberid);
        	System.out.println("동일한 아이디를 가진 회원 존재 여부(존재 시 true 출력) : " + temp2.isPresent());
        	
        	if(temp2.isEmpty()) {
        		createOAuth2(oAuth2Response, memberid);
        	} else {
        		Member member = temp2.get();
        		updateOAuth2(oAuth2Response, member);
        	}
        	
        } else {
        	//객체 업데이트
        	
        }
        Member existMemberEmail = temp1.get();
        
        
        
        
        if(temp.isEmpty()) {
        	
        }
        Member existMemberId = temp.get();
        

        
        
        if(existMemberId == null) {
        	
        }
        */
        
        return new OAuth2CustomUser(oAuth2Response, role);
    }
    
    
    // 이용자가 없을 때 새로운 이용자를 생성해주는 메소드
	public Member createOAuth2(OAuth2Response oAuth2Response, String memberid) {
		Member member = new Member();
		
		member.setMemberid(memberid);
		member.setMembername(memberid);
		member.setMembernick(memberid);
		member.setEmail(oAuth2Response.getEmail());
		member.setCreateDate(LocalDateTime.now());
//		member.setPassword(passwordEncoder.encode(memberCreateForm.getPassword1()));
		this.memberRepository.save(member);
		return member;
	}
	
	// 동일한 이메일 주소를 가진 이용자가 있을 때 이용자의 정보(provider)를 업데이트 해주는 메소드
	public Member updateOAuth2(OAuth2Response oAuth2Response, Member member) {
		
		member.setProvider(oAuth2Response.getProvider());
		member.setProviderId(oAuth2Response.getProviderID());
//		member.setPassword(passwordEncoder.encode(memberCreateForm.getPassword1()));
		this.memberRepository.save(member);
		return member;
	}
	

    /*
    private Member saveOrUpdate(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        Optional<Member> temp = memberRepository.findMemberByEmail(email);
        Member member = temp.get();
        
        return memberRepository.save(member);
    }
    
    


	*/

	
}
