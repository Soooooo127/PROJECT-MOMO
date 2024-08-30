package com.momo.member;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.momo.DataNotFoundException;
import com.momo.auth.OAuth2Member;
import com.momo.image.Image;
import com.momo.member.profile.Profile;
import com.momo.member.profile.ProfileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	
	private final MemberRepository memberRepository;
	private final ProfileRepository profileRepository;
	private final PasswordEncoder passwordEncoder;

	
	public Member create(MemberCreateForm memberCreateForm) {
		Member member = new Member();
		member.setMemberid(memberCreateForm.getMemberid());
		member.setMembername(memberCreateForm.getMembername());
		member.setMembernick(memberCreateForm.getMembernick());
		member.setEmail(memberCreateForm.getEmail());
		member.setCreateDate(LocalDateTime.now());
		member.setPassword(passwordEncoder.encode(memberCreateForm.getPassword1()));
		this.memberRepository.save(member);
		
		Profile profile = new Profile();
		profile.setGender(memberCreateForm.getGender());
		profile.setMbti(memberCreateForm.getMbti());
		profile.setContent(memberCreateForm.getContent());
		profile.setAuthor(member);
		this.profileRepository.save(profile);
		
		member.setProfile(profile);
		this.memberRepository.save(member);
		
		return member;
	}
	

	/*
	// Builder를 사용한 객체 생성 메소드
	public Integer save(MemberAddRequest mar) {
		return memberRepository.save(Member.builder()
				.memberid(mar.getMemberid())
				.password(passwordEncoder.encode(mar.getPassword1()))
				.membername(mar.getMembername())
				.membernick(mar.getMembernick())
				.email(mar.getEmail())
				.build()).getNo();
	}
	*/
	

	public Member getMember(String memberid) {
		Optional<Member> member = this.memberRepository.findBymemberid(memberid);
		if (member.isPresent()) {
			return member.get();
		} else {
			return null;
		}
	}
	
	public Member getMember(Integer memberno) {
		Optional<Member> member = this.memberRepository.findById(memberno);
		if (member.isPresent()) {
			return member.get();
		} else {
			return null;
		}
	}
	
	public Member getMemberByEmail (String email) {
		Optional<Member> member = this.memberRepository.findMemberByEmail(email);
		if(member.isPresent()) {
			return member.get();
		} else {
			throw new DataNotFoundException("siteUser not found by email");
		}
	}
	
	public void makeFriends(String myId, Member friendMember) {
		Optional<Member> temp = memberRepository.findBymemberid(myId);
		Member myMember = temp.get();
		myMember.getFriend().add(friendMember);
		memberRepository.save(myMember);
		
	}
	
	// 회원정보 업데이트 메소드
	public void updateMember(String memberid, Profile profile) {
		Optional<Member> _member = this.memberRepository.findBymemberid(memberid);
		if(_member.isPresent()) {
			Member member = _member.get();
			member.setProfile(profile);
			this.memberRepository.save(member);
		} else {
			throw new DataNotFoundException("site member not found");
		}
	}
	
	public void updateMember(String memberid, Image image) {
		Optional<Member> _member = this.memberRepository.findBymemberid(memberid);
		if(_member.isPresent()) {
			Member member = _member.get();
			member.setImage(image);
			this.memberRepository.save(member);
		} else {
			throw new DataNotFoundException("site member not found");
		}
	}
	
	// 회원정보 업데이트(일반) 메소드
	public void updateMember(Member member) {
		memberRepository.save(member);
	}
	
	// 비밀번호 체크 메소드
	public boolean checkPassword(Member member, String password) {
		Optional<Member> _member = memberRepository.findById(member.getNo());
		Member foundMember = _member.get();
		String savedPassword = foundMember.getPassword();
		boolean result = false;
		
		if(passwordEncoder.matches(password, savedPassword)) {
			result = true;
		}
		
		return result;
	}

	


}
