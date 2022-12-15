package org.heigit.gischat.domain;

import java.time.*;
import java.util.*;

public class Chat {

	private static final String DEFAULT_ID = "1";

	private String title;
	private final String id;
	private final List<ChatMessage> messages = new ArrayList<>();

	public Chat(String title) {
		this.id = DEFAULT_ID;
		this.title = title;
	}

	public Chat(String id, String title) {
		this.id = id;
		this.title = title;
	}
	public String getId() {
		return this.id;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<ChatMessage> getMessages() {
		return Collections.unmodifiableList(this.messages);
	}

	public ChatMessage addMessage(Instant time, String user, String text) {
		ChatMessage newMessage = new ChatMessage(time, new User(user), text);
		messages.add(newMessage);
		Collections.sort(messages);
		return newMessage;
	}

	public ChatMessage addMessage(Instant time, User user, String text) {
		ChatMessage newMessage = new ChatMessage(time, user, text);
		this.messages.add(newMessage);
		Collections.sort(this.messages);
		return newMessage;
	}
}
