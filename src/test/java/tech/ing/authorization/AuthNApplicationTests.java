package tech.ing.authorization;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthNApplicationTests {

	@Autowired
	private MockMvc mvc;

	@Test
	void contextLoads() {
	}

	@Test
	@DisplayName("GET /hello-world unauthenticated")
	public void whenGetHelloWorldUnauthenticatedThenReturn403() throws Exception {
		mvc.perform(get("/hello-world"))
				.andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("POST /v1/users/login with correct credentials")
	public void whenLoginWithCorrectCredentialsThenReturn200WithJwtToken() throws Exception {
		mvc.perform(
				post("/v1/user/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\n" +
						"\t\"username\": \"user\",\n" +
						"\t\"password\": \"12345\"\n" +
						"}"))
			.andExpect(status().isOk())
			.andExpect(header().exists("Jwt-Token"));
	}

	@Test
	@DisplayName("POST /v1/users/login with incorrect password")
	public void whenLoginWithIncorrectPasswordThenReturn400() throws Exception {
		mvc.perform(
				post("/v1/user/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\n" +
								"\t\"username\": \"user\",\n" +
								"\t\"password\": \"1234\"\n" +
								"}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", Matchers.is("Invalid username / password")));
	}

	@Test
	@DisplayName("POST /v1/users/login with incorrect user")
	public void whenLoginWithIncorrectUserThenReturn400() throws Exception {
		mvc.perform(
				post("/v1/user/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\n" +
								"\t\"username\": \"use\",\n" +
								"\t\"password\": \"12345\"\n" +
								"}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", Matchers.is("Invalid username / password")));
	}
}
