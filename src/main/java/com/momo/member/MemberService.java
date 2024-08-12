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
	
	public Member create(String memberid, String password, String membernick, String email) {
		Member member = new Member();
		member.setMemberid(memberid);
		member.setMembernick(membernick);
		member.setEmail(email);
		member.setCreateDate(LocalDateTime.now());
		member.setPassword(passwordEncoder.encode(password));
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
	
	public void makeFriends(String myId, Member friendMember) {
		Optional<Member> temp = memberRepository.findBymemberid(myId);
		Member myMember = temp.get();
		myMember.getFriend().add(friendMember);
		memberRepository.save(myMember);
		
	}

}
