package org.heigit.gischat.domain;

import java.time.*;

public record ChatMessage(Instant time, String user, String text) implements Comparable<ChatMessage> {

	public ChatMessage(Instant time, String user, String text) {
		this.time = time;
		this.user = normalizeUser(user);
		this.text = text;
	}

	private static String normalizeUser(String originalUser) {
		if (originalUser == null) {
			throw new IllegalArgumentException("user must not be null");
		}
		if (originalUser.isBlank()) {
			throw new IllegalArgumentException("user must not be blank");
		}
		String[] allParts = originalUser.trim().split("\\s+");
		return String.join(" ", allParts);
	}

	@Override
	public int compareTo(ChatMessage other) {
		return time().compareTo(other.time());
	}

}
