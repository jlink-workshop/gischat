package org.heigit.gischat.domain;

public record User(String name) {
	public User {
		if (name == null) {
			throw new IllegalArgumentException("user must not be null");
		}
		if (name.isBlank()) {
			throw new IllegalArgumentException("user must not be blank");
		}
		String[] parts = name.trim().split("\\s+");
		name = String.join(" ", parts);
	}
}
