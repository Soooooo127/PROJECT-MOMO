package com.momo.chatting;

import java.security.Principal;
import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.momo.member.Member;
import com.momo.member.MemberService;
import com.momo.restaurant.et.EatTogether;
import com.momo.restaurant.et.EatTogetherService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {

	private final ChatService chatService;
	private final MemberService memberService;
	private final EatTogetherService eatTogetherService; 

	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/chat/list/{no}")
	//대화방에 같이먹기 신청자 목록 출력
	public String get(@PathVariable("no")Integer no, Model model,Principal principal) {
	
		System.out.println("채팅 화면 초기 진입 확인");
		
		//같이먹기 리스트 검색 
	EatTogether eatTogether = eatTogetherService.getET(no);
	List<Member> etMemberList	= eatTogether.getPrtmember();
	model.addAttribute("etMemberList", etMemberList); //모델에 저장 
	return "/chat/chatting";
		
	}
	
	//대화 내용을 saveMessage로 보내고 저장함
	@MessageMapping("/message.sendMessage")
	@SendTo("/topic/message")
	public Message sendMessage(Message message, Principal principal) {
	
		System.out.println("========================================");
		System.out.println("MessageMapping 컨트롤러 진입 확인");
		//보내는 사람 = 로그인 한 사람
		Member sender = this.memberService.getMember(principal.getName()); 
		//받는 사람
		Member receiver = this.memberService.getMember(message.getChatroom().getMember2().getMemberid());
		
		//메세지
		String content = message.getContent();
		
		return chatService.saveMessage(sender, receiver, content);
	}
	
	
	//기존 대화방, 대화내용 불러오기
	@GetMapping("/chat/message/{member2}")
	@ResponseBody
	public List<Message> getMessage(@PathVariable("member2")String member2,Principal principal){
		System.out.println("=======================================================");
		System.out.println("대화방,대화내용 불러오기 확인");
		List<Message> mList = this.chatService.getMessage(principal.getName(),member2);	
		return mList;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
}
