package com.momo.board.free.posting;

import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.momo.board.free.comment.FreeCommentForm;
import com.momo.member.Member;
import com.momo.member.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/free")
@RequiredArgsConstructor
public class FreePostingController {
	
	// 아싸라비야콜롬비야 짝짝짝~~!!! (20240809 16:50 수정)

	private final FreePostingService freePostingService;
	private final MemberService memberService;
	
	@GetMapping("/list")
	public String getList(Model model, @RequestParam(value = "page", defaultValue = "0") int page,Principal principal) {
		Page<FreePosting> paging = freePostingService.getList(page);
		model.addAttribute("paging", paging);
		Member member = memberService.getMember(principal.getName());
		model.addAttribute("member", member);
		return "free/free_list";
	}
	
	/* 페이징 없는 List
	@GetMapping("/list")
	public String getList(Model model) {
		List<FreePosting> freePostingList = freePostingService.getList();
		System.out.println(freePostingList.isEmpty());
		model.addAttribute("freePostingList", freePostingList);
		return "/free/free_list";
	}
	*/
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/create")
	public String create(FreePostingForm freePostingForm, Model model) {
		List<FreePosting> freePostingList = freePostingService.getList();
		System.out.println(freePostingList.isEmpty());
		for(int i=0 ; i < freePostingList.size() ; i++ ) {
			System.out.println(freePostingList.get(i).getSubject());
		}
		model.addAttribute("freePostingList", freePostingList);
		return "/free/free_form";
	}
	
	@PostMapping("/create")
	public String create(@Valid FreePostingForm freePostingForm, BindingResult bindingResult, Principal principal) {
		
		if(bindingResult.hasErrors()) {
			return "/free/free_form";
		}
		
		Member member = memberService.getMember(principal.getName());
		freePostingService.create(freePostingForm.getSubject(), member,
				member.getMembernick(), freePostingForm.getContent());
		return "redirect:/free/list";
	}
	
	@GetMapping("/update/{pno}")
	public String update(@PathVariable("pno") Integer pno, FreePostingForm freePostingForm) {
		FreePosting freePosting = freePostingService.getPosting(pno);
		
		freePostingForm.setSubject(freePosting.getSubject());
		freePostingForm.setContent(freePosting.getContent());
		
		return "free/free_form";
	}
	
	@PostMapping("/update/{pno}")
	public String update(@Valid FreePostingForm freePostingForm, BindingResult bindingResult,
			@PathVariable("pno") Integer pno) {
		
		if(bindingResult.hasErrors()) {
			return "/free/free_form";
		}
		
		FreePosting freePosting = freePostingService.getPosting(pno);
		
		freePostingService.update(freePosting, freePostingForm.getSubject(), freePostingForm.getContent());
		
		return "redirect:/free/detail/{pno}";
	}
	
	
	@GetMapping("/detail/{pno}")
	public String detail(Model model, @PathVariable("pno") Integer pno, FreeCommentForm freeCommentForm,Principal principal) {
		FreePosting freePosting = freePostingService.getPosting(pno);
		model.addAttribute("freePosting", freePosting);
		Member member = memberService.getMember(principal.getName());
		model.addAttribute("member", member);
		System.out.println(freePosting.toString());		
		return "free/free_detail"; 
	}
	
	/*구따봉
	@GetMapping("/ddabong/{pno}")
	public String ddabong(Model model, @PathVariable("pno") Integer pno, FreeCommentForm freeCommentForm) {
		FreePosting freePosting = freePostingService.updateDdabong(pno);
		
		model.addAttribute("freePosting", freePosting);
		System.out.println(freePosting.toString());		
		return "free/free_detail"; 
	}
	*/
	
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/ddabong/{pno}")
    public String ddabong(Principal principal, @PathVariable("pno") Integer pno) {
        FreePosting freePosting = freePostingService.getPosting(pno);
        Member member = memberService.getMember(principal.getName());
        freePostingService.ddabong(freePosting, member);
        return "redirect:/free/detail/{pno}";
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/nope/{pno}")
    public String nope(Principal principal, @PathVariable("pno") Integer pno) {
    	FreePosting freePosting = freePostingService.getPosting(pno);
    	Member member = memberService.getMember(principal.getName());
    	freePostingService.nope(freePosting, member);
    	return "redirect:/free/detail/{pno}";
    }
    
    /*
	@GetMapping("/nope/{pno}")
	public String nope(Model model, @PathVariable("pno") Integer pno, FreeCommentForm freeCommentForm) {
		FreePosting freePosting = freePostingService.updateNope(pno);
		
		model.addAttribute("freePosting", freePosting);
		System.out.println(freePosting.toString());		
		return "free/free_detail"; 
	}
	*/

	
	
	
	/*
	@GetMapping("/update/{pno}")
	public String update(Model model, @RequestParam(value = "subject") String subject, 
			@RequestParam(value = "content") String content) {
		
		
		
		return null;
		
	}
	*/
	
	@GetMapping("/delete/{pno}")
	public String delete(@PathVariable("pno") Integer pno) {
		freePostingService.delete(pno);
		return "redirect:/free/list";
	}
	
	//찜 테스트를 위한 임시 메소드
	@GetMapping("/nope/1")
	public String jjimTest() {
		System.out.println("테스트 메소드에 들어왔습니다.");
		FreePosting freePosting = freePostingService.getPosting(1);
		int nope = freePosting.getCnt();
		nope++;
		freePosting.setCnt(nope);
		freePostingService.update(freePosting);
		
		return "redirect:/rest/";
		
	}
	
	//찜 테스트를 위한 임시 메소드2
	@GetMapping("/nope/2")
	public String jjimTest2() {
		System.out.println("테스트 메소드에 들어왔습니다.");
		FreePosting freePosting = freePostingService.getPosting(1);
		int nope = freePosting.getCnt();
		nope--;
		freePosting.setCnt(nope);
		freePostingService.update(freePosting);
		
		return "redirect:/rest/";
		
	}
}
