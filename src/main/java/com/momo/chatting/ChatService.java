package com.momo.chatting;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.momo.member.Member;
import com.momo.member.MemberService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {

	
	private final ChatRoomRepository chatRoomRepository;   
	private final MessageRepository messageRepository;
	private final MemberService memberService; 
	
	//대화방 생성
	public ChatRoom createChat(Member member1 , Member member2) {
		ChatRoom room = new ChatRoom();
		room.setMember1(member1);
		room.setMember2(member2);
	return	this.chatRoomRepository.save(room);
	}
	
	//대화내용 저장
	public Message saveMessage(Member sender, Member receiver,String content) {
		Message message = new Message();
		message.setContent(content);
		message.setSendtime(LocalDateTime.now());
		
		Optional<ChatRoom> _chatRoom1 = this.chatRoomRepository.findByMember1AndMember2(sender, receiver);
		
		if(_chatRoom1.isPresent()) {  //해당 멤버들의 채팅방이 존재하면
			
			ChatRoom chatRoom = _chatRoom1.get();
			message.setSender(sender);
			message.setChatroom(chatRoom);
			return this.messageRepository.save(message);
			
		}else {
			
			Optional<ChatRoom> _chatRoom2 = this.chatRoomRepository.findByMember1AndMember2(receiver, sender);
			
			if(_chatRoom2.isPresent()) {
				ChatRoom chatRoom = _chatRoom2.get();
				message.setSender(sender);
				message.setChatroom(chatRoom);
				return this.messageRepository.save(message);
				}else { //채팅방이 없다면 생성
					ChatRoom chatRoom = createChat(sender, receiver);
					message.setSender(sender);
					message.setChatroom(chatRoom);
					return this.messageRepository.save(message);
					}
		
		
					}
	
						} //대화내용 저장 메소드 끝
	
	//기존 대화내용 불러오기
	public List<Message> getMessage(String member1, String member2){
		System.out.println("=================================");
		System.out.println("기존 대화내용 리스트 서비스 진입 확인");
		//유저들
		Member getMember1 = this.memberService.getMember(member1);		
		Member getMember2 = this.memberService.getMember(member2);
		
		//해당 유저들을 기준으로 채팅방 검색
		Optional<ChatRoom> chatRoom = this.chatRoomRepository.findByMember1AndMember2(getMember1, getMember2);	
		
		//채팅방이 있으면
		if(chatRoom.isPresent()) {
			return chatRoom.get().getMessage();
		}else {
			//유저 보내고 받는 사람 바꿔서 한번 더 검색
			Optional<ChatRoom> chatRoom2 = this.chatRoomRepository.findByMember1AndMember2(getMember2, getMember1);
			//검색한 채팅방이 있으면
			if(chatRoom2.isPresent()) {  
				return chatRoom.get().getMessage();
			}else {
				return new ArrayList<>();
		
			}
			}
		
		
		
	}
	
	
	
	
	
}
