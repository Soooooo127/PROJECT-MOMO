package com.momo.board.inquiry.comment;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.momo.board.inquiry.posting.InquiryPosting;
import com.momo.user.SiteUser.SiteUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InquiryCommentService {

	private final InquiryCommentRepository inquiryCommentRepository;
	
	public void create(InquiryPosting inquiryPosting, String content, SiteUser siteUser) {
		InquiryComment comment = new InquiryComment();
		comment.setContent(content);
		comment.setCreateDate(LocalDateTime.now());
		comment.setInquiryPosting(inquiryPosting);
		comment.setAuthor(siteUser);
		this.inquiryCommentRepository.save(comment);
	}
	
	public void delete(Integer id) {
		this.inquiryCommentRepository.deleteById(id);
	}
}
