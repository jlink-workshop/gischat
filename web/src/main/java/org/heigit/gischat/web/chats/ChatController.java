package org.heigit.gischat.web.chats;

import java.time.*;
import java.time.temporal.*;

import io.swagger.v3.oas.annotations.*;
import org.heigit.gischat.domain.*;
import org.heigit.gischat.web.repository.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.*;

@RestController
@RequestMapping(path = ChatController.CHATS_PATH)
public class ChatController {

	public static final String CHATS_PATH = "/api/chats/";

	private ChatController(ChatRepository repository) {
		this.repository = repository;
	}

	private final ChatRepository repository;

	@Operation(summary = "Get a chat by id with all its messages")
	@GetMapping(path = "{id}")
	public ChatResponse getChat(@PathVariable String id) {
		ensureIdIsValid(id);
		Chat chat = repository.findById(Integer.parseInt(id)).orElseThrow();
		return new ChatResponse(chat);
	}

	@Operation(summary = "Post a new message to a chat with id")
	@PostMapping(path = "{id}/messages")
	public ChatMessageResponse postMessageToChat(
		@PathVariable String id,
		@RequestBody ChatMessageRequest messageRequest
	) {
		ensureIdIsValid(id);
		Chat chat = repository.findById(Integer.parseInt(id)).orElseThrow();
		ChatMessage newMessage = addMessageToChat(messageRequest, chat);
		repository.save(chat);
		return new ChatMessageResponse(newMessage);
	}

	private ChatMessage addMessageToChat(ChatMessageRequest messageRequest, Chat chat) {
		return chat.addMessage(Instant.now().truncatedTo(ChronoUnit.SECONDS), messageRequest.getUser(), messageRequest.getText());
	}

	private void ensureIdIsValid(@PathVariable String id) {
		if (!id.equals("1") && !id.equals("2")) {
			throw new ResponseStatusException(
				HttpStatus.NOT_FOUND, String.format("no chat with id [%s]", id)
			);
		}
	}
}
