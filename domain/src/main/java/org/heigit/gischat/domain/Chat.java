package org.heigit.gischat.domain;

import java.time.*;
import java.util.*;

public class Chat {

	private static final String DEFAULT_ID = "1";

	private String title;

	private final List<ChatMessage> messages = new ArrayList<>();

	public Chat(String title) {
		this.title = title;
	}

	public String getId() {
		return DEFAULT_ID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<ChatMessage> getMessages() {
		return Collections.unmodifiableList(messages);
	}

	public ChatMessage addMessage(Instant time, String user, String text) {
		ChatMessage newMessage = new ChatMessage(time, user, text);
		messages.add(newMessage);
		Collections.sort(messages);
		return newMessage;
	}
}
