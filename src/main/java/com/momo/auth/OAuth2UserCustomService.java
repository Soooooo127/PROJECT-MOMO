package com.momo.auth;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
	private final OAuth2MemberRepository oAuth2MemberRepository;
	
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//    	System.out.println("userRequest - clientId : " + userRequest.getClientRegistration().getClientId());
//    	System.out.println("userRequest - clientName : " + userRequest.getClientRegistration().getClientName());
//    	System.out.println("userRequest - clientSecret : " + userRequest.getClientRegistration().getClientSecret());
    	System.out.println("userRequest - RegistrationId : " + userRequest.getClientRegistration().getRegistrationId());
    	
    	
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("oAuth2User : " + oAuth2User.getAttributes());
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        String role = "ROLE_SOCIAL";
        String membernick = null;
        
        System.out.println();
        
        if(registrationId.equals("google")) {
        	oAuth2Response = new OAuth2ResponseGoogle(oAuth2User.getAttributes());
        } else if(registrationId.equals("naver")) {
        	oAuth2Response = new OAuth2ResponseNaver(oAuth2User.getAttributes());
        } else {
        	return null;
        }

        // 동일한 이메일을 가진 회원이 있는지 확인
        Optional<Member> tempMember = memberRepository.findMemberByEmail(oAuth2Response.getEmail());
        System.out.println("동일한 이메일 주소를 가진 회원 존재 여부(존재 시 true 출력) : " + tempMember.isPresent());
        
        
        // 동일 이메일을 가진 회원이 있다면 해당 SNS로 등록을 완료했는지 확인 
        if(tempMember.isPresent()) {

        	Member member = tempMember.get();
        	System.out.println("회원의 아이디 : " + member.getMemberid());
        	
        	addSnsInfo(member, oAuth2Response, registrationId);

        // 동일 이메일을 가진 회원이 없기 때문에 신규 가입 후 해당 SNS로 등록을 완료 
        } else {
        	
        	addSnsInfo(createMember(oAuth2Response), oAuth2Response, registrationId);
        	
        }
        
        // 회원의 권한을 체크
        Optional<Member> tempMember2 = memberRepository.findMemberByEmail(oAuth2Response.getEmail());
        Member finalMember = tempMember2.get();
        
        if(finalMember.getCreateDate() != null) {
        	role = "ROLE_MEMBER";
        }
        
        
        return new OAuth2CustomUser(oAuth2Response, role, finalMember);
    }
    
    
    // SNS 로그인한 회원이 기존 회원이 아닌 경우 신규 가입하는 메소드
	public Member createMember(OAuth2Response oAuth2Response) {
		System.out.println("OAuth2 신규 회원 가입 메소드에 진입하였습니다.");
		System.out.println("oAuth2 Provider : " + oAuth2Response.getProvider());
		
		String memberid = null;
		
		if(oAuth2Response.getProvider().equals("naver")) {
			String _memberid = oAuth2Response.getProviderId();
			memberid = "naver" + _memberid.substring(0, 10);
			System.out.println("네이버 로그인 가입자의 아이디 " + oAuth2Response.getProvider());
		} else {
			memberid = oAuth2Response.getProvider() + oAuth2Response.getProviderId();
		}
		
    	String membernameBefore = oAuth2Response.getName();
    	String membername = membernameBefore.replaceAll(" ", "");
    	System.out.println("임시 아이디 : " + memberid);
    	System.out.println("임시 이름, 닉네임 수정 전 : " + membernameBefore);
    	System.out.println("임시 이름, 닉네임 : " + membername);
    	
    	Member member = new Member();
    	member.setMemberid(memberid);
    	member.setMembername(membername);
    	member.setMembernick(membername);
    	member.setEmail(oAuth2Response.getEmail());
    	
		memberRepository.save(member);
		
		return member;
		
	}
	
	// SNS 로그인 계정 정보를 신규 생성하는 메소드(이 Service 내에서만 사용)
	private OAuth2Member createOAuth2Member(Member member, OAuth2Response oAuth2Response, String registrationId) {
		System.out.println("-----SNS계정정보 신규 생성  메소드 진입");
		
		OAuth2Member oAuth2Member = new OAuth2Member();
		oAuth2Member.setProvider(registrationId);
		oAuth2Member.setProviderId(oAuth2Response.getProviderId());
		oAuth2Member.setConnectDate(LocalDateTime.now());
		oAuth2Member.setMember(member);
		oAuth2MemberRepository.save(oAuth2Member);
		
		return oAuth2Member;
	}
	
	
	// SNS 로그인 계정 정보를 업데이트 하는 메소드
	public void addSnsInfo(Member member, OAuth2Response oAuth2Response, String registrationId) {
		System.out.println("-----SNS계정 업데이트 메소드 진입");
		System.out.println("RegistrationId : " + registrationId);
		
		Optional<List<OAuth2Member>> snsListTemp = oAuth2MemberRepository.findByMemberNo(member.getNo());
		List<OAuth2Member> snsList = snsListTemp.get();
		
		// 폼 회원가입한 후 SNS 연결을 한 번도 하지 않은 회원의 경우
		if(member.getOauth2MemberList() == null) {
			System.out.println("SNSList 존재 여부 : 없습니다.");
			
			createOAuth2Member(member, oAuth2Response, registrationId);
			
		
			
		// 폼 회원가입한 후 SNS 연결을 한 적이 있거나, SNS을 통한 회원가입을 한 회원	
		} else {
			System.out.println("SNSList 존재 여부 : 있습니다.");
			
			List<String> providerList = new ArrayList<>();
			for(int i=0 ; i < snsList.size() ; i++) {
				providerList.add(snsList.get(i).getProvider());
			}
			
			// 해당 registrationId로 연결된 정보가 있는지 확인
			if(providerList.contains(registrationId)) {
				System.out.println(registrationId + "이 리스트에 있습니다.");
				
			} else {
				System.out.println(registrationId + "이 리스트에 없습니다.");
				createOAuth2Member(member, oAuth2Response, registrationId);
				
			}
			
		}

		memberRepository.save(member);
	
	}
	



	
}
