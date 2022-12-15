package org.heigit.gischat.web.repository;

import java.time.*;

import java.util.*;

import org.heigit.gischat.domain.*;
import org.heigit.gischat.web.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ChatRepositoryTests {

	@Mock
	GischatConfiguration configuration;

	ChatRepository repository;

	@BeforeEach
	void initRepository() {
		Mockito.when(configuration.getChatTitle()).thenReturn("Just a Chat");
		repository = new InMemoryChatRepository(configuration);
	}

	@Test
	void findFirstChat() {
		Optional<Chat> chat = repository.findById(1);
		assertThat(chat).isPresent();
		assertThat(chat.get().getId()).isEqualTo("1");
		assertThat(chat.get().getTitle()).isEqualTo("Just a Chat");
		assertThat(chat.get().getMessages()).isEmpty();
	}
	@Test
	void findSecondChat() {
		Optional<Chat> chat = repository.findById(2);
		assertThat(chat).isPresent();
		assertThat(chat.get().getId()).isEqualTo("2");
		assertThat(chat.get().getTitle()).isEqualTo("Just another Chat");
		assertThat(chat.get().getMessages()).isEmpty();
	}

	@Test
	void getTheOneChatTwiceWillCreateAFreshOneIfNotSaved() {
		Chat chat1 = repository.findById(1).orElseThrow();
		chat1.addMessage(Instant.now(), "anyUser", "any text");
		Chat chat2 = repository.findById(1).orElseThrow();
		assertThat(chat2.getMessages()).isEmpty();
	}


	@Test
	void savingAChatRetrievedByIdWillSaveMessages() {
		Optional<Chat> chat1 = repository.findById(1);
		Instant now = Instant.now();
		chat1.ifPresent(chat->  {
			chat.addMessage(now, "anyUser", "any text");
			repository.save(chat);
		});
		Optional<Chat> chat2 = repository.findById(1);
		chat2.ifPresent(chat->  {
			assertThat(chat.getMessages()).hasSize(1);
			ChatMessage message = chat.getMessages().get(0);
			assertThat(message.time()).isEqualTo(now);
			assertThat(message.user()).isEqualTo("anyUser");
			assertThat(message.text()).isEqualTo("any text");
		});

	}


}
