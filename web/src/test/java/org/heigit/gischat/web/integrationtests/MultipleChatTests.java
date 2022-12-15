package org.heigit.gischat.web.integrationtests;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.support.ui.*;

import static org.assertj.core.api.Assertions.assertThat;

class MultipleChatTests extends IntegrationTests {
	@ParameterizedTest
	@ValueSource(ints = {1}) // Chat rooms
	void chatsExists(int chatId) {
		driver.get(chatUrl(chatId));
		assertThat(driver.getTitle()).contains("MapChat");

		waitAndAssert(
			driver -> {
				WebElement chatTitle = driver.findElement(By.id("chatTitle"));
				assertThat(chatTitle.getText()).isEqualTo(String.format("Mapathon %d", chatId));
			}
		);
	}

	@Test
	void noCrossPosting() {
		driver.get(chatUrl(1));
		String first = driver.getWindowHandle();

		driver.switchTo().newWindow(WindowType.WINDOW);
		driver.get(chatUrl(2));

		WebElement userNameInput = driver.findElement(By.id("userName"));
		userNameInput.sendKeys("John Doe");

		WebElement messageTextInput = driver.findElement(By.id("messageText"));
		messageTextInput.sendKeys("This is a message");

		WebElement sendButton = driver.findElement(By.id("sendMessageButton"));
		sendButton.click();


		// Switch back to first window

		driver.switchTo().window(first);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		waitAndAssert(
			driver -> {
				WebElement messages = driver.findElement(By.id("messages"));
				assertThat(messages.getText()).doesNotContain("John Doe");
				assertThat(messages.getText()).doesNotContain("This is a message");
			}
		);
	}

	void forStealingCode() {
		driver.get(indexUrl());
		String first = driver.getWindowHandle();

		// Send message in new window

		driver.switchTo().newWindow(WindowType.WINDOW);
		driver.get(indexUrl());

		WebElement userNameInput = driver.findElement(By.id("userName"));
		userNameInput.sendKeys("John Doe");

		WebElement messageTextInput = driver.findElement(By.id("messageText"));
		messageTextInput.sendKeys("This is a message");

		WebElement sendButton = driver.findElement(By.id("sendMessageButton"));
		sendButton.click();


		// Switch back to first window

		driver.switchTo().window(first);

		waitAndAssert(
			driver -> {
				WebElement messages = driver.findElement(By.id("messages"));
				assertThat(messages.getText()).contains("John Doe");
				assertThat(messages.getText()).contains("This is a message");
			}
		);
	}

}
