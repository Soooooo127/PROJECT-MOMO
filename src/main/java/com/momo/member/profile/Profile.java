package com.momo.member.profile;

import com.momo.member.Member;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Profile {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer no;

	private String imgUrl;
	
	private String mbti;
	
	private String content;
	
	@OneToOne(mappedBy = "profile", cascade = CascadeType.REMOVE)
	private Member member;
	
	

}
