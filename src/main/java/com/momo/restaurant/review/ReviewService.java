package com.momo.restaurant.review;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.momo.DataNotFoundException;
import com.momo.board.free.comment.FreeComment;
import com.momo.member.Member;
import com.momo.member.MemberService;
import com.momo.restaurant.RestRepository;
import com.momo.restaurant.Restaurant;

import groovy.cli.Option;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final RestRepository restRepository;
	//private final MemberService memberService;
	
	public List<Review> getList(Restaurant rest){
		return this.reviewRepository.findByRest(rest);
	}
	
	public List<Review> reviewList(Integer no){
		List<Review> reviewList = reviewRepository.findAll();
		return reviewList;
	}
	
	
	public Review getReview(Integer no) {
		
		Optional<Review> re = reviewRepository.findById(no);
		if(re.isPresent()) {
			return re.get();
		}else {
			throw new DataNotFoundException("데이터가 없습니다");
		}

	}
	
	public void create(Integer no, Member member, String content, Integer star) {
		Restaurant rest = new Restaurant();
		Optional<Restaurant> findRest = restRepository.findById(no);
		rest = findRest.get();
		
		Review review = new Review();
		review.setStar(star);
		review.setAuthor(member);
		review.setRest(rest);
		review.setContent(content);
		review.setMembernick(member.getMembernick());
		review.setCreateDate(LocalDateTime.now());
		reviewRepository.save(review);
		
	}
	
	public void delete(Integer no) {
		reviewRepository.deleteById(no);
	}
	
	public void update(Review review, String content) {
		review.setContent(content);
		review.setUpdateDate(LocalDateTime.now());
		reviewRepository.save(review);
	}
	

	public void ddabong(Review review, Member member) {
		review.getDdabong().add(member);
		reviewRepository.save(review);
	}
	
}
