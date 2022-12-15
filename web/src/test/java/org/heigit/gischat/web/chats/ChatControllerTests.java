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

	private final Chat firstChat = new Chat("a chat");
	private final Chat secondChat = new Chat("2", "Just another Chat");
	private MockMvc mockMvc;

	@BeforeEach
	void setup(@Autowired WebApplicationContext wac) {
		mockMvc = webAppContextSetup(wac).build();
		when(sessionRepository.findById(1)).thenReturn(Optional.of(firstChat));
		when(sessionRepository.findById(2)).thenReturn(Optional.of(secondChat));
	}

	@Test
	void getFirstChat() throws Exception {
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
	void getSecondChat() throws Exception {
		String getChatUrl = ChatController.CHATS_PATH + "{id}";
		MvcResult result = mockMvc.perform(get(getChatUrl, "2"))
								  .andExpect(status().isOk())
								  .andReturn();

		Map<String, Object> jsonResponse = responseAsMap(result);
		assertEquals("Just another Chat", jsonResponse.get("title"));
		assertEquals("2", jsonResponse.get("id"));
		assertEquals(Collections.emptyList(), jsonResponse.get("messages"));
	}

	@Test
	void postMessageToFirstChat() throws Exception {
		String postMessageUrl = ChatController.CHATS_PATH + "{id}" + "/messages";
		String messageRequest = requestAsString(new ChatMessageRequest("frank", "a message text"));
		MvcResult result = mockMvc.perform(post(postMessageUrl, "1")
											   .contentType(APPLICATION_JSON_VALUE)
											   .content(messageRequest))
								  .andExpect(status().isOk())
								  .andReturn();

		verify(sessionRepository).save(firstChat);

		Map<String, Object> jsonResponse = responseAsMap(result);
		assertNotNull(jsonResponse.get("time"));
		assertEquals("frank", jsonResponse.get("user"));
		assertEquals("a message text", jsonResponse.get("text"));
	}

	@Test
	void postMessageToSecondChat() throws Exception {
		String postMessageUrl = ChatController.CHATS_PATH + "{id}" + "/messages";
		String messageRequest = requestAsString(new ChatMessageRequest("another frank", "another message text"));
		MvcResult result = mockMvc.perform(post(postMessageUrl, "2")
											   .contentType(APPLICATION_JSON_VALUE)
											   .content(messageRequest))
								  .andExpect(status().isOk())
								  .andReturn();

		verify(sessionRepository).save(secondChat);

		Map<String, Object> jsonResponse = responseAsMap(result);
		assertNotNull(jsonResponse.get("time"));
		assertEquals("another frank", jsonResponse.get("user"));
		assertEquals("another message text", jsonResponse.get("text"));
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
