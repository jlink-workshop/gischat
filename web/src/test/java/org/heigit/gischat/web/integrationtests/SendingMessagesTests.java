package org.heigit.gischat.web.integrationtests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;

import static org.assertj.core.api.Assertions.*;

class SendingMessagesTests extends IntegrationTests {

	@Test
	void sentMessageIsReceivedInOtherWindow() {
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
