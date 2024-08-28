package com.momo.chatting;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.momo.member.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {

	
	private final ChatRoomRepository chatRoomRepository; 
	private final MessageRepository messageRepository;
	
	
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
	
	
	
	
	
	
	
}
