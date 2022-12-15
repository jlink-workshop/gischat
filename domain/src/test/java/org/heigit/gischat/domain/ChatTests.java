package org.heigit.gischat.domain;

import java.time.*;
import java.util.*;
import java.util.stream.*;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.*;

class ChatTests {

	@Nested
	class Title {

		@Test
		void createChatWithTitle() {
			Chat chat = new Chat("My little chat");
			assertThat(chat.getId()).isEqualTo("1");
			assertThat(chat.getTitle()).isEqualTo("My little chat");
		}

		@Test
		void changeChatTitle() {
			Chat chat = new Chat("My little chat");
			chat.setTitle("My other chat");
			assertThat(chat.getTitle()).isEqualTo("My other chat");
		}
	}

	@Nested
	class Messages {

		Chat chat = new Chat("My little chat");

		@Test
		void noMessagesInitially() {
			assertThat(chat.getMessages()).isEmpty();
		}

		@Test
		void userNameMustNotBeEmpty() {
			Instant now = Instant.now();
			assertThatThrownBy( () -> chat.addMessage(now, "", "Hi."))
				.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void userNameMustNotContainOnlyWhitespace() {
			Instant now = Instant.now();
			assertThatThrownBy( () -> chat.addMessage(now, "   ", "Hi."))
				.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void userNameIsFullyTrimmed() {
			Instant now = Instant.now();
			ChatMessage message = chat.addMessage(now, " my \t name  ", "Hi.");
			assertThat(message.user().name()).isEqualTo("my name");
		}

		@Test
		void addingSingleMessage() {
			Instant now = Instant.now();
			ChatMessage newMessage = chat.addMessage(now, "frank", "Hi.");
			assertThat(newMessage.time()).isEqualTo(now);
			assertThat(newMessage.user().name()).isEqualTo("frank");
			assertThat(newMessage.text()).isEqualTo("Hi.");

			assertThat(chat.getMessages()).hasSize(1);
			ChatMessage message = chat.getMessages().get(0);
			assertThat(message).isEqualTo(newMessage);
		}

		@Test
		void addingSeveralMessages() {
			Instant now = Instant.now();
			chat.addMessage(now, "frank", "Hi.");
			chat.addMessage(now.plusSeconds(10), "emmy", "Hi too");
			chat.addMessage(now.plusSeconds(20), "frank", "Nice to see you");

			assertThat(chat.getMessages()).hasSize(3);
		}

		@Test
		void messagesAreOrderedByTime() {
			Instant firstTime = Instant.now();
			Instant secondTime = firstTime.plusSeconds(10);
			chat.addMessage(secondTime, "emmy", "Hi too");
			chat.addMessage(firstTime, "frank", "Hi.");

			List<Instant> times = chat.getMessages().stream().map(ChatMessage::time)
									  .collect(Collectors.toList());
			assertThat(times).containsExactly(
				firstTime, secondTime
			);
		}

		@Test
		void messageAreEqualIfAllFieldsAreTheSame() {
			Instant now = Instant.now();
			ChatMessage message1 = chat.addMessage(now, "frank", "Hi.");
			ChatMessage message2 = chat.addMessage(now, "frank", "Hi.");
			assertThat(message1).isEqualTo(message2);
		}

		@Test
		void messagesDifferIfAnyFieldIsDifferent() {
			Instant now = Instant.now();
			ChatMessage message = chat.addMessage(now, "frank", "Hi.");

			assertThat(message).isNotEqualTo(
				chat.addMessage(now.plusSeconds(10), "frank", "Hi.")
			);
			assertThat(message).isNotEqualTo(
				chat.addMessage(now, "michael", "Hi.")
			);
			assertThat(message).isNotEqualTo(
				chat.addMessage(now, "frank", "Hello.")
			);
		}

		@Test
		void acceptsUserObject() {
			Instant now = Instant.now();
			var message = chat.addMessage(now, new User("user"), "Hi.");
			assertThat(message.user()).isInstanceOf(User.class);
			assertThat(message.user().name()).isEqualTo("user");
		}

	}
}
