package org.heigit.gischat.web.frontend;

import org.heigit.gischat.web.GischatApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest(
	properties = {
		"gischat.pollingInterval=1234",
		"gischat.pychatterUrl=https://pychatter.example.com",
	}
)
@WebAppConfiguration
@ContextConfiguration(classes = {GischatApplication.class})
class ConfigJsControllerTests {

	private MockMvc mockMvc;

	@BeforeEach
	void setup(@Autowired WebApplicationContext wac) {
		mockMvc = webAppContextSetup(wac).build();
	}

	@Test
	void getConfigJs() throws Exception {
		String configJsPath = "/js/config.js";
		MvcResult result = mockMvc.perform(get(configJsPath))
								  .andExpect(status().isOk())
								  .andReturn();

		String configJs = result.getResponse().getContentAsString();
		assertThat(configJs).contains("pollingInterval: 1234");
		assertThat(configJs).contains("pychatterUrl: \"https://pychatter.example.com\"");
	}

}
