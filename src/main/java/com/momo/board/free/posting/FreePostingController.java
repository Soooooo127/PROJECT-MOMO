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

import com.momo.board.free.comment.FreeComment;
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
	public String getList(Model model, @RequestParam(value = "page", defaultValue = "0") int page, Principal principal) {
		Page<FreePosting> paging = freePostingService.getList(page);
		model.addAttribute("paging", paging);
		
		if(principal != null) {
			Member member = memberService.getMember(principal.getName());
			model.addAttribute("member", member);
		}

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
		return "free/free_form";
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
		
		if(principal != null) {
			Member member = memberService.getMember(principal.getName());
			model.addAttribute("member", member);
		}

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
        String _memberid = principal.getName();
        freePostingService.ddabong(freePosting, _memberid);
        return "redirect:/free/detail/{pno}";
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/nope/{pno}")
    public String nope(Principal principal, @PathVariable("pno") Integer pno) {
    	FreePosting freePosting = freePostingService.getPosting(pno);
    	String _memberid = principal.getName();
    	freePostingService.nope(freePosting, _memberid);
    	return "redirect:/free/detail/{pno}";
    }
    
    // 댓글 개수 입력을 위한 임시 메소드(추후에는 필요 없음)
    @GetMapping("/totalComment")
    public void calTotalComment() {
    	System.out.println("===========================댓글 수 계산 메소드에 진입하였습니다===========================");
    	List<FreePosting> _list = freePostingService.getList();
    	
    	for(int i = 0 ; i < _list.size() ; i++) {
    		FreePosting _free = _list.get(i);
    		List<FreeComment> _commentList = _free.getFreeCommentList();
    		int totalC = _commentList.size();
    		
    		System.out.println(i + "번째 _free 게시물의 총 댓글 수 : " + totalC);
    		
    		int totalCR = 0;
    		
    		for(int j = 0 ; j < _commentList.size() ; j++) {
    			FreeComment _comment = _commentList.get(j);
    			totalCR += _comment.getFreeCommentReplyList().size();
    			
    			System.out.println(j + "번째 _comment 게시물의 누적 댓글 수 : " + totalCR);
    			
    			if(j == _commentList.size()-1) {
    				System.out.println("반복 완료! 댓글의 누적 댓글의댓글 수 : " + totalCR);
    			}
    		}
    		
    		totalC += totalCR;
    		System.out.println(i + "번째 _free 게시물의 누적 댓글 수 : " + totalC);
    		_free.setTotalComment(totalC);
    		freePostingService.update(_free);
    		
    		if(i == _list.size()-1) {
    			System.out.println("모든 연산이 완료되었습니다.");
    		}
    		
    		
    	}
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
	
	@PostMapping("/search")
	public List<FreePosting> searchList(@RequestParam(value = "keyword") String keyword) {
		return null;
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
