package com.momo.member.profile;

import org.hibernate.annotations.ColumnDefault;

import com.momo.member.Member;

import groovy.transform.Generated;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
