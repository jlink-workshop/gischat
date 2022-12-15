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
	void getTheOneChat() {
		Chat chat = repository.findOne();
		assertThat(chat.getId()).isEqualTo("1");
		assertThat(chat.getTitle()).isEqualTo("Just a Chat");
		assertThat(chat.getMessages()).isEmpty();
	}
	@Test
	void getChatById() {
		Optional<Chat> chat = repository.findById(1);
		assertThat(chat).isPresent();
		assertThat(chat.get().getId()).isEqualTo("1");
		assertThat(chat.get().getTitle()).isEqualTo("Just a Chat");
		assertThat(chat.get().getMessages()).isEmpty();
	}

	@Test
	void getTheOneChatTwiceWillCreateAFreshOneIfNotSaved() {
		Chat chat1 = repository.findOne();
		chat1.addMessage(Instant.now(), "anyUser", "any text");
		Chat chat2 = repository.findOne();
		assertThat(chat2.getMessages()).isEmpty();
	}

	@Test
	void savingAChatWillSaveMessages() {
		Chat chat1 = repository.findOne();
		Instant now = Instant.now();
		chat1.addMessage(now, "anyUser", "any text");
		repository.save(chat1);

		Chat chat2 = repository.findOne();
		assertThat(chat2.getMessages()).hasSize(1);

		ChatMessage message = chat2.getMessages().get(0);
		assertThat(message.time()).isEqualTo(now);
		assertThat(message.user()).isEqualTo("anyUser");
		assertThat(message.text()).isEqualTo("any text");
	}
}
