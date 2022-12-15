package org.heigit.gischat.web.chats;

import java.time.*;

import org.heigit.gischat.domain.*;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.*;

class ChatResponseTests {
	@Test
	void mapChatWithoutMessages() {
		Chat chat = new Chat("my chat title");
		ChatResponse response = new ChatResponse(chat);

		assertThat(response.getId()).isEqualTo("1");
		assertThat(response.getTitle()).isEqualTo("my chat title");
		assertThat(response.getMessages()).isEmpty();
	}

	@Test
	void mapChatWithMessages() {
		Chat chat = new Chat("my chat title");
		chat.addMessage(Instant.now(), "user1", "text1");
		chat.addMessage(Instant.now(), "user2", "text2");
		ChatResponse response = new ChatResponse(chat);

		assertThat(response.getMessages()).hasSize(2);
		assertThat(response.getMessages().stream()
		    .map(ChatMessageResponse::getUser))
			.containsExactly("user1", "user2");
	}
}
