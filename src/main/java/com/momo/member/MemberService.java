package com.momo.member;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.momo.DataNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	
	private final MemberRepository memberRepository;
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
		return member;
	}

	public Member getMember(String memberid) {
		Optional<Member> member = this.memberRepository.findBymemberid(memberid);
		if (member.isPresent()) {
			return member.get();
		} else {
			throw new DataNotFoundException("siteuser not found");
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
	
	public void updateMember(String memberid, String membernick) {
		Optional<Member> _member = this.memberRepository.findBymemberid(memberid);
		if(_member.isPresent()) {
			Member member = _member.get();
			member.setMembernick(membernick);
			this.memberRepository.save(member);
		} else {
			throw new DataNotFoundException("site member not found");
		}
	}

}
