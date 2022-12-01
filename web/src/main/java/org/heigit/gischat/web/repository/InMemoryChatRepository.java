package org.heigit.gischat.web.repository;

import org.heigit.gischat.domain.*;
import org.heigit.gischat.web.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Service
public class InMemoryChatRepository implements ChatRepository {

	private final GischatConfiguration configuration;
	private Chat theChat;

	@Autowired
	public InMemoryChatRepository(GischatConfiguration configuration) {
		this.configuration = configuration;
		this.theChat = createChat();
	}

	@Override
	public synchronized Chat findOne() {
		return copyChat();
	}

	private Chat copyChat() {
		Chat copy = new Chat(theChat.getTitle());
		for (ChatMessage message : theChat.getMessages()) {
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
