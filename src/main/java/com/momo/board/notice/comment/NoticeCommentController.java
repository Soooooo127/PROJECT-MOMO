package com.momo.board.notice.comment;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.momo.board.notice.posting.NoticePosting;
import com.momo.board.notice.posting.NoticePostingService;
import com.momo.board.user.SiteUser;
import com.momo.board.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notice/comment")
public class NoticeCommentController {

	private final NoticeCommentService noticeCommentService;
	private final NoticePostingService NoticePostingService; //해당하는 공지 글 번호에 답글을 달아야 하기때문
	private final UserService userService;
	
	
	//답글등록 
	@PreAuthorize("isAuthenticated()") //해당 어노테이션이 붙으면 로그인 한 경우에만 실행한다는 뜻임
	@PostMapping("/create/{no}")
	public String commentCreate(Model model, @PathVariable("no") Integer no,
			@Valid NoticeCommentForm noticeCommentForm, BindingResult bindingResult, Principal principal ) {
		
		NoticePosting noticePosting  = this.NoticePostingService.getNoticePosting(no); //공지글번호
		SiteUser siteUser = this.userService.getUser(principal.getName()); //작성자 가져오기
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("noticePosting", noticePosting);
			return "notice_detail";
		}
		this.noticeCommentService.create(noticePosting,noticeCommentForm.getContent(),siteUser);
	
		return String.format("redirect:/notice/posting/detail/{no}", no);
		
	
	}
	
	
	
	
}
