package com.momo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.momo.member.MemberRepository;
<<<<<<< HEAD
=======
import com.momo.member.MemberService;
>>>>>>> de2daf9711a126a233565551bd50866e82acced6
import com.momo.restaurant.RestService;

@SpringBootTest
class MomoApplicationTests {
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private RestService restService;
	
	@Test
	void contextLoads() {
<<<<<<< HEAD
		
		/*Member member = new Member();
=======
		/*
		Member member = new Member();
>>>>>>> de2daf9711a126a233565551bd50866e82acced6
		member.setMemberid("admin");
		member.setPassword("12345");
		member.setMembernick("관리자");
		member.setEmail("admin@pandaworld.com");
		member.setCreateDate(LocalDateTime.now());
		memberRepository.save(member);
		*/
		
		for(int i = 0; i<=10; i++) {
			String name = String.format("테스트 데이터 : [%03d]", i);
			String category = "중식";
			this.restService.create(name, category);
		}
		
		for(int i = 0 ; i < 5 ; i++) {
			String name = String.format("테스트가게[%03d]", i);
			String category = "한식";
			this.restService.create(name, category);
		}
		
		*/
	}

}
