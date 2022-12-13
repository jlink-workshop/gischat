package org.heigit.gischat.web;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class GischatApplicationTests {

	@Autowired
	GischatConfiguration gischatConfiguration;

	@Test
	void initializeContext() {
		assertThat(gischatConfiguration.getChatTitle()).isEqualTo("Mapathon 1");
	}
}
