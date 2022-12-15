package org.heigit.gischat.domain;

import java.time.*;

public record ChatMessage(Instant time, User user, String text) implements Comparable<ChatMessage> {

	public ChatMessage(Instant time, User user, String text) {
		this.time = time;
		this.user = user;
		this.text = text;
	}

	@Override
	public int compareTo(ChatMessage other) {
		return time().compareTo(other.time());
	}

}
