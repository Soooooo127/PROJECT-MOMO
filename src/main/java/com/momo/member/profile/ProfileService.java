package com.momo.member.profile;

import java.io.IOException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.momo.DataNotFoundException;
import com.momo.image.ImageService;
import com.momo.member.Member;
import com.momo.member.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService {

	
	private final ProfileRepository profileRepository;
	private final MemberRepository memberRepository;
	
	
	public void modifyProfile(String memberid, String gender, String content, String mbti){

		Optional<Member> _member = this.memberRepository.findBymemberid(memberid);
		Member member = new Member();
		if(_member.isPresent()) {
			member = _member.get();
		} else {
			throw new DataNotFoundException("회원이 없습니다");
		}
		
		Optional<Profile> _profile = this.profileRepository.findByAuthor(member);
		
		if(_profile.isEmpty()) {
			Profile profile = new Profile();
			profile.setGender(gender);
			profile.setMbti(mbti);
			profile.setContent(content);
			profile.setAuthor(member);
			this.profileRepository.save(profile);

		} else if(_profile.isPresent()) {
			Profile profile = _profile.get();
			profile.setGender(gender);
			profile.setMbti(mbti);
			profile.setContent(content);
			this.profileRepository.save(profile);
		}
		
		 
		
	}  
	
	public Profile getProfile(Member member) {
		Optional<Profile> profile = this.profileRepository.findByAuthor(member);
		if(profile.isPresent()) {
			return profile.get();
		} else {
			return null;
		}
	}
	

}
