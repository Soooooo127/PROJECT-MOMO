package com.momo.restaurant.jjim;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.momo.member.Member;
import com.momo.member.MemberRepository;
import com.momo.restaurant.Restaurant;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JjimService {

	private final JjimRepository jjimRepository;
	private final MemberRepository memberRepository;

	
	public Jjim get(Member member, Restaurant rest) {
		
		Optional<Jjim> _jjim	=  this.jjimRepository.findByMemberAndRest(member, rest);
	    
		if(_jjim.isPresent()) {System.out.println("찜 있음");
	   		return _jjim.get();
	   		
	    }else{
	    	System.out.println("찜 없음");
	    	return null;
	    }
	
	}
	
	
	/*
	public Jjim findByMemberAnd(Member member){
		return jjimRepository.findByMember(member);
	}
	
	public void jjim(Restaurant rest, Member member) {
		rest.getJjim().add(member);
		this.jjimRepository.save(rest);
	}

	
	public void NoJjim(Restaurant rest, Member member) {
		rest.getJjim().remove(member);
		this.jjimRepository.remove(rest);
	}
   */
	
	public void create(Member member, Restaurant rest) {
		Jjim jjim = new Jjim();
		jjim.setMember(member);
		jjim.setRest(rest);
		this.jjimRepository.save(jjim);
		
		
	}
	
	public void delete(Member member, Restaurant rest) {
		Jjim jjim = this.get(member, rest);
		this.jjimRepository.delete(jjim);
	}
	
	
	public List<Jjim> getList(Restaurant rest){
		return this.jjimRepository.findByRest(rest);
		
	}
}
