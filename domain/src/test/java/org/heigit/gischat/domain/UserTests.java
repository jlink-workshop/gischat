package org.heigit.gischat.domain;

import org.junit.jupiter.api.*;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTests {
	@Test
	void createsNewUserWithUsername() {
		User user = new User("test");
		assertThat(user.name()).isEqualTo("test");
	}

	@Test
	void usernameIsNotAllowedToBeNull() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new User(null);
		});
	}

	@Test
	void usernameIsNotAllowedToBeBlank() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new User("");
		});
	}
	@Test
	void trimsUserNameStart() {
		User user = new User("  test");
		assertThat(user.name()).isEqualTo("test");
	}
	@Test
	void trimsUserNameEnd() {
		User user = new User("test ");
		assertThat(user.name()).isEqualTo("test");
	}
	@Test
	void trimsUserNameMiddle() {
		User user = new User("hallo  test");
		assertThat(user.name()).isEqualTo("hallo test");
	}
}
