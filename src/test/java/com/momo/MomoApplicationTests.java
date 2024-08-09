package com.momo;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.momo.member.Member;
import com.momo.member.MemberRepository;
import com.momo.member.MemberService;

@SpringBootTest
class MomoApplicationTests {
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Test
	void contextLoads() {
		
		Member member = new Member();
		member.setMemberid("admin");
		member.setPassword("12345");
		member.setMembernick("관리자");
		member.setEmail("admin@pandaworld.com");
		member.setCreateDate(LocalDateTime.now());
		
		memberRepository.save(member);
		
		
	}

}
