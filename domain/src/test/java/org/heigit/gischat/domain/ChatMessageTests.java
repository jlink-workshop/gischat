package org.heigit.gischat.domain;

import org.junit.jupiter.api.*;

import java.time.*;

import static org.assertj.core.api.Assertions.*;

class ChatMessageTests {
	@Test
	void userPropertyIsInstanceOfUserClass() {
		ChatMessage message = new ChatMessage(Instant.now(), new User("name"), "message");
		assertThat(message.user()).isInstanceOf(User.class);
	}

}
