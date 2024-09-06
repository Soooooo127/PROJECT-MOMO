package com.momo.friend;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.momo.member.Member;
import com.momo.member.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/friend")
public class FriendController {

	
	private final FriendService friendService; 
	private final MemberService memberService;
	
	
	
	//친구 목록
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/list")
	public String list(Principal principal, Model model) {
		 Member member = this.memberService.getMember(principal.getName());
		 model.addAttribute("member", member);
		return "friend/friend";
	}
	
	
	//친구 추가  
		@PreAuthorize("isAuthenticated()")
		@GetMapping("/create/{no}")
		public String createFriend(Principal principal, @PathVariable("no")Integer friendno, Model model) {
		
			Member friendMember  = this.memberService.getMember(friendno); //친구 아이디 검색
			 Member myMember = memberService.getMember(principal.getName()); //나의 로그인한 정보 있는지 유무 확인 후 객체 생성 
			 model.addAttribute("myMember", myMember);  //객체 생성해서 모델에 저장 
			 
			friendService.createFriend(principal.getName(),friendMember ); //로그인한 정보, 친구 객체를 매개변수로
			return "redirect:/friend/list";
		}
		
	
		
		//친구 삭제
		@PreAuthorize("isAuthenticated()")
		@PostMapping("/delete")
		public String deleteFriend(Principal pricipal,@RequestParam(value = "friendid") String friendid) {
		
			Member myid = memberService.getMember(pricipal.getName());  //로그인 한 이이디 검색
			Member myfriendid = memberService.getMember(friendid);  //친구 아이디 검색
			friendService.deleteFriend(myid, myfriendid);
			 return "redirect:/friend/list";
			
		}
		
		//친구 삭제
		@PreAuthorize("isAuthenticated()")
		@GetMapping("/delete/{no}")
		public String deleteFriend(Principal pricipal, @PathVariable("no") Integer no) {
			
			Member myid = memberService.getMember(pricipal.getName());  //로그인 한 이이디 검색
			Member myfriendid = memberService.getMember(no);  //친구 아이디 검색
			friendService.deleteFriend(myid, myfriendid);
			return "redirect:/friend/list";
			
		}
	
	
	
	
	
	
	
}
