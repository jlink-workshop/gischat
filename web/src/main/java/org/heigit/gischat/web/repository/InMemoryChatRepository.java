package org.heigit.gischat.web.repository;

import java.util.*;

import org.heigit.gischat.domain.*;
import org.heigit.gischat.web.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Service
public class InMemoryChatRepository implements ChatRepository {

	private final GischatConfiguration configuration;
	private Chat theChat;
	private final Map<Integer, Chat> theChats = new HashMap<>();

	@Autowired
	public InMemoryChatRepository(GischatConfiguration configuration) {
		this.configuration = configuration;
		this.theChat = createChat();
		this.theChats.put(1, this.theChat);
	}

	@Override
	public synchronized Chat findOne() {
		return copyChat(theChat);
	}

	@Override
	public Optional<Chat> findById(int id) {
		return Optional.of(theChats.get(id));
	}

	private Chat copyChat(Chat chat) {
		Chat copy = new Chat(chat.getTitle());
		for (ChatMessage message : chat.getMessages()) {
			copy.addMessage(message.time(), message.user(), message.text());
		}
		return copy;
	}

	private Chat createChat() {
		return new Chat(configuration.getChatTitle());
	}

	@Override
	public void save(Chat chat) {
		theChat = chat;
	}
}
