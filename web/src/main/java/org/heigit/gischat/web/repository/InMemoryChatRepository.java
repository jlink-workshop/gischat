package org.heigit.gischat.web.repository;

import java.util.*;

import org.heigit.gischat.domain.*;
import org.heigit.gischat.web.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Service
public class InMemoryChatRepository implements ChatRepository {

	private final GischatConfiguration configuration;
	private final Map<Integer, Chat> theChats = new HashMap<>();

	@Autowired
	public InMemoryChatRepository(GischatConfiguration configuration) {
		this.configuration = configuration;
		this.theChats.put(1, createChat());
		this.theChats.put(2, createChat("2", "Just another Chat"));
	}

	@Override
	public synchronized Optional<Chat> findById(int id) {
		return Optional.of(copyChat(theChats.get(id)));
	}

	private Chat copyChat(Chat chat) {
		Chat copy = new Chat(chat.getId(), chat.getTitle());
		for (ChatMessage message : chat.getMessages()) {
			copy.addMessage(message.time(), message.user(), message.text());
		}
		return copy;
	}

	private Chat createChat() {
		return new Chat(configuration.getChatTitle());
	}

	private Chat createChat(String id, String title) {
		return new Chat(id, title);
	}
	@Override
	public void save(Chat chat) {
		theChats.put(1,chat);
	}
}
