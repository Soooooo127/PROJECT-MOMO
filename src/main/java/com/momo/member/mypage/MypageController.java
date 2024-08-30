package com.momo.member.mypage;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.momo.board.ask.posting.AskPosting;
import com.momo.board.ask.posting.AskPostingService;
import com.momo.board.free.posting.FreePosting;
import com.momo.board.free.posting.FreePostingService;
import com.momo.member.Member;
import com.momo.member.MemberService;
import com.momo.restaurant.jjim.Jjim;
import com.momo.restaurant.jjim.JjimService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MypageController {

	
	private final FreePostingService freePostingService;
	private final AskPostingService askPostingService;
	private final MemberService memberService;
	private final JjimService jjimService;
	
	// 마이페이지 내 게시물 보기
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/myPosting")
	public String getMyPosting(Model model, @RequestParam(value="freeSubject", defaultValue="")String freeSubject, 
			                   @RequestParam(value="askSubject", defaultValue="")String askSubject
			                   , @RequestParam(value="page", defaultValue="0") int page
			                   , @RequestParam(value="pg", defaultValue="0") int pg 
			                   ,Principal principal) {
		Member member = this.memberService.getMember(principal.getName());
		Page<FreePosting> myFreePosting = this.freePostingService.getMyList(member, freeSubject, page);
		Page<AskPosting> myAskPosting = this.askPostingService.getMyList(member, askSubject, pg);
		model.addAttribute("myFreePosting", myFreePosting);
     	model.addAttribute("freeSubject", freeSubject );
		model.addAttribute("myAskPosting", myAskPosting);
		model.addAttribute("askSubject", askSubject);
		return "/mypage/mypage_myPosting";
	}
	
	// 마이페이지 내 가게 찜 목록 보기
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/myJjim")
	public String getMyJjimList(Model model, @RequestParam(value="page", defaultValue="0")int page, Principal principal) {
		Member member = this.memberService.getMember(principal.getName());
		Page<Jjim> paging = this.jjimService.getMyJjimList(member, page);
		model.addAttribute("paging", paging);
		return "/mypage/mypage_myJjim";
	}
}
