package com.momo.member;

import org.springframework.security.core.userdetails.User;

public class MemberDetail {
	
//	 extends user 해야함
	private Member member;
	
	/*
    public SecurityUser(Member member) {
        super(member.getUsername(), member.getPassword(), AuthorityUtils.createAuthorityList(member.getRole().toString()));

        log.info("SecurityUser member.username = {}", member.getUsername());
        log.info("SecurityUser member.password = {}", member.getPassword());
        log.info("SecurityUser member.role = {}", member.getRole().toString());

        this.member = member;
    }
    */

}
