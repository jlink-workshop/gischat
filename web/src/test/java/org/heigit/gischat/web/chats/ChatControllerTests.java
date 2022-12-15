package org.heigit.gischat.web.chats;

import java.util.*;

import com.fasterxml.jackson.databind.*;
import org.heigit.gischat.domain.*;
import org.heigit.gischat.web.*;
import org.heigit.gischat.web.repository.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.test.context.*;
import org.springframework.test.context.web.*;
import org.springframework.test.web.servlet.*;
import org.springframework.web.context.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@SpringBootTest
@WebAppConfiguration
@ContextConfiguration(classes = {GischatApplication.class})
class ChatControllerTests {

	@MockBean
	ChatRepository sessionRepository;

	private final Chat chat = new Chat("a chat");
	private MockMvc mockMvc;

	@BeforeEach
	void setup(@Autowired WebApplicationContext wac) {
		mockMvc = webAppContextSetup(wac).build();
		when(sessionRepository.findById(1)).thenReturn(Optional.of(chat));
	}

	@Test
	void getChat() throws Exception {
		String getChatUrl = ChatController.CHATS_PATH + "{id}";
		MvcResult result = mockMvc.perform(get(getChatUrl, "1"))
								  .andExpect(status().isOk())
								  .andReturn();

		Map<String, Object> jsonResponse = responseAsMap(result);
		assertEquals("a chat", jsonResponse.get("title"));
		assertEquals("1", jsonResponse.get("id"));
		assertEquals(Collections.emptyList(), jsonResponse.get("messages"));
	}

	@Test
	void postMessage() throws Exception {
		String postMessageUrl = ChatController.CHATS_PATH + "{id}" + "/messages";
		String messageRequest = requestAsString(new ChatMessageRequest("frank", "a message text"));
		MvcResult result = mockMvc.perform(post(postMessageUrl, "1")
											   .contentType(APPLICATION_JSON_VALUE)
											   .content(messageRequest))
								  .andExpect(status().isOk())
								  .andReturn();

		verify(sessionRepository).save(chat);

		Map<String, Object> jsonResponse = responseAsMap(result);
		assertNotNull(jsonResponse.get("time"));
		assertEquals("frank", jsonResponse.get("user"));
		assertEquals("a message text", jsonResponse.get("text"));
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> responseAsMap(MvcResult result) throws Exception {
		return new ObjectMapper().readValue(result.getResponse().getContentAsString(), Map.class);
	}

	private String requestAsString(ChatMessageRequest request) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		return ow.writeValueAsString(request);
	}
}
