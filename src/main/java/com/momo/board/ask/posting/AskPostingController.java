package com.momo.board.ask.posting;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.momo.board.ask.comment.AskComment;
import com.momo.board.ask.comment.AskCommentForm;
import com.momo.board.ask.comment.AskCommentService;
import com.momo.member.Member;
import com.momo.member.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/askPosting")
public class AskPostingController {

	private final AskPostingService askPostingService;
	private final MemberService momoMemberService;
	private final AskCommentService askCommentService;
	//질문게시글 목록 페이지로 띄우기
	@GetMapping("/list")
	public String listAskPosting(Model model , @RequestParam(value = "page" , defaultValue = "0") int page) {
		Page<AskPosting> paging = this.askPostingService.getList(page);
		model.addAttribute("paging", paging);
		return "askPosting_list";
	}
	//질문게시글 상세보기 + 내부 답변댓글 페이지화
	@GetMapping("/detail/{no}")
	public String detailAskPosting(Model model , @PathVariable("no") Integer no , AskCommentForm askCommentForm 
			, @RequestParam(value = "page" , defaultValue = "0") int page) {
		AskPosting askPosting = this.askPostingService.getAskPosting(no);
		Page<AskComment> paging = this.askCommentService.askCommentPage(askPosting, page);
		model.addAttribute("paging", paging);
		
		model.addAttribute("askPosting", askPosting);
		return "askPosting_detail";
	}
	
	//사용자 인증 + 질문게시글쓰기 화면
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/create")
	public String createAskPosting(AskPostingForm askPostingForm) {
		return "askPosting_form";
	}
	
	//사용자 인증 + 질문게시글 작성완료
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create")
	public String createAskPosting(@Valid AskPostingForm askPostingForm 
			, BindingResult bindingResult , Principal principal) {
		if(bindingResult.hasErrors()) {
			return "askPosting_form";
		}
		Member momoMember = this.momoMemberService.getMember(principal.getName());
		this.askPostingService.create(askPostingForm.getSubject(), askPostingForm.getContent() , momoMember);
		return "redirect:/askPosting/list";
	}
	
	//사용자 인증 + 질문게시글 수정 화면
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/update/{no}")
	public String updateAskPosting(AskPostingForm askPostingForm , @PathVariable("no") Integer no , Principal principal){
		AskPosting askPosting = this.askPostingService.getAskPosting(no);
		if(!askPosting.getMembernick().getMemberid().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "수정 권한이 없습니다.");
		}
		askPostingForm.setSubject(askPosting.getSubject());
		askPostingForm.setContent(askPosting.getContent());
		
		return "askPosting_form";
	}
	
	//사용자 인증 + 질문게시글 수정완료
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/update/{no}")
	public String updateAskPosting(@Valid AskPostingForm askPostingForm , BindingResult bindingResult 
			, @PathVariable("no") Integer no , Principal principal) {
		if(bindingResult.hasErrors()) {
			return "askPosting_form";
		}
		
		AskPosting askPosting = this.askPostingService.getAskPosting(no);
		if(!askPosting.getMembernick().getMemberid().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "수정 권한이 없습니다.");
		}
		
		this.askPostingService.update(askPosting , askPostingForm.getSubject() , askPostingForm.getContent());
		return String.format("redirect:/askPosting/detail/%s" , no);
	}
	
	//사용자 인증 + 질문게시글 삭제
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{no}")
	public String deleteAskPosting(Principal principal , @PathVariable("no") Integer no) {
		AskPosting askPosting = this.askPostingService.getAskPosting(no);
		if(!askPosting.getMembernick().getMemberid().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "삭제 권한이 없습니다");
		}
		this.askPostingService.delete(askPosting);
		return "redirect:/askPosting/list";
	}
	
	//질문게시글 추천기능
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/voteDdabong/{no}")
	public String askPostingVote(Principal principal , @PathVariable("no") Integer no) {
		AskPosting askPosting = this.askPostingService.getAskPosting(no);
		Member momoMember = this.momoMemberService.getMember(principal.getName());
		this.askPostingService.voteDdabong(askPosting, momoMember);
		return String.format("redirect:/askPosting/detail/%s", no);
	}
	
	//질문게시글 비추천기능
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/voteNope/{no}")
	public String askPostingNope(Principal principal , @PathVariable("no") Integer no) {
		AskPosting askPosting = this.askPostingService.getAskPosting(no);
		Member momoMember = this.momoMemberService.getMember(principal.getName());
		this.askPostingService.voteNope(askPosting, momoMember);
		return String.format("redirect:/askPosting/detail/%s", no);
	}
	
}
