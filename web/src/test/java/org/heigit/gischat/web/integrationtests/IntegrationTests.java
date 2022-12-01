package org.heigit.gischat.web.integrationtests;

import java.time.*;
import java.util.*;
import java.util.function.*;

import io.github.bonigarcia.wdm.*;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.test.web.server.*;
import org.springframework.test.context.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
abstract class IntegrationTests {

	@BeforeAll
	static void installWebdriver() {
		WebDriverManager.chromedriver().setup();
	}

	@LocalServerPort
	protected Integer port;

	protected WebDriver driver;

	@BeforeEach
	void initWebDriver() {
		ChromeOptions options = new ChromeOptions();
		if (Objects.equals(System.getenv("CI"), "true")) {
			options.setHeadless(true);
		}
		options.addArguments("--window-size=400,800");
		driver = new ChromeDriver(options);
	}

	@AfterEach
	void quitWebDriver() {
		driver.quit();
	}

	protected String indexUrl() {
		return String.format("http://localhost:%d/index.html", port);
	}

	protected WebDriverWait createWait() {
		return new WebDriverWait(driver, Duration.ofSeconds(5));
	}

	protected boolean waitUntil(Function<WebDriver, Boolean> isTrue) {
		return createWait().until(isTrue);
	}

	protected void waitAndAssert(Consumer<WebDriver> assertion) {
		AssertionError[] lastException = new AssertionError[1];
		Function<WebDriver, Boolean> predicate = driver -> {
			try {
				assertion.accept(driver);
				return true;
			} catch (AssertionError assertionError) {
				lastException[0] = assertionError;
				return false;
			}
		};

		try {
			waitUntil(predicate);
		} catch (TimeoutException e) {
			throw lastException[0];
		}
	}
}
