package com.momo.board.free.posting;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.momo.DataNotFoundException;
import com.momo.board.free.comment.FreeCommentRepository;
import com.momo.member.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FreePostingService {

	private final FreePostingRepository freePostingRepository;
	private final FreeCommentRepository freeCommentRepository;
	
	
	public void create(String subject, Member member, String membernick, String content) {
		FreePosting freePosting = new FreePosting();
		
		freePosting.setSubject(subject);
		freePosting.setContent(content);
		freePosting.setMembernick(membernick);
		freePosting.setCreateDate(LocalDateTime.now());
		freePosting.setAuthor(member);
		freePostingRepository.save(freePosting);
		
	}
	
	public void update(FreePosting freePosting, String subject, String content) {
		freePosting.setSubject(subject);
		freePosting.setContent(content);
		freePosting.setUpdateDate(LocalDateTime.now());
		freePostingRepository.save(freePosting);
	}
	
	public Page<FreePosting> getList(int page) {
		List<Sort.Order> sort = new ArrayList<>();
		sort.add(Sort.Order.desc("createDate"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sort));
		return freePostingRepository.findAll(pageable);
	}
	
	public List<FreePosting> getList() {
		List<FreePosting> freePostingList = new ArrayList<>();
		freePostingList = freePostingRepository.findAll();
		return freePostingList;
	}
	
	//나의 자유게시판 글 목록 페이지형식 + 검색 기능
    public Page<FreePosting> getMyList(Member member, String subject, int page) {	
		List<Sort.Order> sort = new ArrayList<>();
		sort.add(Sort.Order.desc("createDate"));
		Pageable pageable = PageRequest.of(page, 5, Sort.by(sort));
		return freePostingRepository.findByAuthorAndSubject(member, subject, pageable);
    }
	

	public FreePosting getPosting(Integer no) {
		Optional<FreePosting> freePosting = freePostingRepository.findById(no);
		
		if(freePosting.isPresent()) {
			FreePosting freePostingNew = new FreePosting();
			freePostingNew = freePosting.get();
			Integer cnt = freePostingNew.getCnt();
			cnt += 1;
			freePostingNew.setCnt(cnt);
			freePostingRepository.save(freePostingNew);
			
			return freePostingNew;
		} else {
			throw new DataNotFoundException("데이터가 없습니다");
		}
	}
	
    public void ddabong(FreePosting freePosting, String memberid) {
    	
    	if(freePosting.getDdabong().isEmpty()) {
    		Set<String> _ddabong = new HashSet<>();
    		_ddabong.add(memberid);
    		freePosting.setDdabong(_ddabong);
    		
    	} else {
    		
    		if(freePosting.getDdabong().contains(memberid)) {
    			freePosting.getDdabong().remove(memberid);
    		} else {
    			freePosting.getDdabong().add(memberid);
    		}
    	}
    	
    	freePostingRepository.save(freePosting);
    }
    
    public void nope(FreePosting freePosting, String memberid) {
    	
    	if(freePosting.getNope().isEmpty()) {
    		Set<String> _nope = new HashSet<>();
    		_nope.add(memberid);
    		freePosting.setNope(_nope);
    		
    	} else {
    		if(freePosting.getNope().contains(memberid)) {
    			freePosting.getNope().remove(memberid);
    		} else {
    			freePosting.getNope().add(memberid);
    		}
    	}
    	
    	freePostingRepository.save(freePosting);
    }


    
    /*
	public FreePosting updateDdabong(Integer no) {
		Optional<FreePosting> freePosting = freePostingRepository.findById(no);
		
		if(freePosting.isPresent()) {
			FreePosting freePostingNew = new FreePosting();
			freePostingNew = freePosting.get();
			Integer ddabong = freePostingNew.getDdabong();
			ddabong += 1;
			freePostingNew.setDdabong(ddabong);
			freePostingRepository.save(freePostingNew);
			
			return freePostingNew;
		} else {
			throw new DataNotFoundException("데이터가 없습니다");
		}
	}
	
	public FreePosting updateNope(Integer no) {
		Optional<FreePosting> freePosting = freePostingRepository.findById(no);
		
		if(freePosting.isPresent()) {
			FreePosting freePostingNew = new FreePosting();
			freePostingNew = freePosting.get();
			Integer nope = freePostingNew.getNope();
			nope += 1;
			freePostingNew.setNope(nope);
			freePostingRepository.save(freePostingNew);
			
			return freePostingNew;
		} else {
			throw new DataNotFoundException("데이터가 없습니다");
		}
	}
	*/
    
	public void delete(Integer no) {
		freePostingRepository.deleteById(no);
	}
	
	// 찜 테스트를 위한 임시 메소드
	public void update(FreePosting freePosting) {
		freePostingRepository.save(freePosting);
	}
}
