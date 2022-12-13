package org.heigit.gischat.web.integrationtests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;

import static org.assertj.core.api.Assertions.*;

class LandingPageTests extends IntegrationTests {

	@Test
	void correctTitleAndChatTitle() {
		driver.get(indexUrl());

		assertThat(driver.getTitle()).contains("GisChat");

		waitAndAssert(
			driver -> {
				WebElement chatTitle = driver.findElement(By.id("chatTitle"));
				assertThat(chatTitle.getText()).isEqualTo("Mapathon 1");
			}
		);
	}

	@Test
	void noMessagesInTheBeginning() {
		driver.get(indexUrl());

		WebElement messages = driver.findElement(By.id("messages"));
		assertThat(messages.getText()).isEmpty();
	}

	@Test
	void sentMessageShowsUp() {
		driver.get(indexUrl());

		WebElement userNameInput = driver.findElement(By.id("userName"));
		userNameInput.sendKeys("John Doe");

		WebElement messageTextInput = driver.findElement(By.id("messageText"));
		messageTextInput.sendKeys("This is a message");

		WebElement sendButton = driver.findElement(By.id("sendMessageButton"));
		sendButton.click();

		waitAndAssert(
			driver -> {
				WebElement messages = driver.findElement(By.id("messages"));
				assertThat(messages.getText()).contains("John Doe");
				assertThat(messages.getText()).contains("This is a message");
			}
		);
	}

}
