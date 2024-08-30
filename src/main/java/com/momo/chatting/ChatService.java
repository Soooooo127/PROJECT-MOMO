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
		System.out.println("===========채팅방 생성 메소드 ============");
		ChatRoom room = new ChatRoom();
		room.setMember1(member1);
		room.setMember2(member2);
		this.chatRoomRepository.save(room);
		return	room;
	}
	
	//대화내용 저장
	public Message saveMessage(Member sender, Member receiver,String content) {
		System.out.println("Sender : " + sender.getMemberid());
		System.out.println("Receiver : " + receiver.getMemberid());
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
		
		System.out.println("member1" + member1);
		System.out.println("member2" + member2);
		//유저들
		Member getMember1 = this.memberService.getMember(member1);		
		Member getMember2 = this.memberService.getMember(member2);
		
		//해당 유저들을 기준으로 채팅방 검색
		Optional<ChatRoom> chatRoom = this.chatRoomRepository.findByMember1AndMember2(getMember1, getMember2);	
		
		//채팅방이 있으면
		if(chatRoom.isPresent()) {
			System.out.println("********채팅방이 있습니다***********");
			return chatRoom.get().getMessage();
		}else {
			System.out.println("********채팅방이 없습니다***********");
			//유저 보내고 받는 사람 바꿔서 한번 더 검색
			Optional<ChatRoom> chatRoom2 = this.chatRoomRepository.findByMember1AndMember2(getMember2, getMember1);
			//검색한 채팅방이 있으면
			if(chatRoom2.isPresent()) {  
				System.out.println("********22222 채팅방이 있습니다***********");
				return chatRoom2.get().getMessage();
			}else {
				System.out.println("********2222222채팅방이 없습니다***********");
				ChatRoom room = this.createChat(getMember1, getMember2);
				List<Message> messageList = new ArrayList<>();
				room.setMessage(messageList);
				if(messageList.isEmpty()) {
					System.out.println("비어있슴다");
				} 
				 return  room.getMessage();
			}
			}

	} //기존대화내용 불러오기 메소드 끝
	
	
	
		
		
		
		
		
		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
