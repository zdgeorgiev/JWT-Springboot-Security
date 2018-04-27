package jwt.security;

import jwt.security.entity.RoleType;
import jwt.security.entity.User;
import jwt.security.model.RegisterUserBindingModel;
import jwt.security.repository.RoleRepository;
import jwt.security.repository.UserRepository;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;
import java.util.ArrayList;

import static jwt.security.config.SecurityConstants.*;
import static jwt.security.util.JsonUtils.asJsonString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class BaseSecurityTestSuite {

	protected static final String REG_USER_USERNAME = "user";
	protected static final String REG_USER_PASSWORD = "user";
	protected static final String ADMIN_USER_USERNAME = "admin";
	protected static final String ADMIN_USER_PASSWORD = "admin";

	protected MockMvc mvc;

	@Autowired
	protected WebApplicationContext context;

	@Autowired
	protected Filter springSecurityFilterChain;

	@Autowired
	protected UserRepository userRepository;

	@Autowired
	protected RoleRepository roleRepository;

	@Before
	public void setup() throws Exception {
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.addFilters(springSecurityFilterChain)
				.build();

		register(REG_USER_USERNAME, REG_USER_PASSWORD, RoleType.USER);
		register(ADMIN_USER_USERNAME, ADMIN_USER_PASSWORD, RoleType.ADMIN);
	}

	protected void register(String username, String password, RoleType role) throws Exception {

		RegisterUserBindingModel model = new RegisterUserBindingModel();
		model.setUsername(username);
		model.setPassword(password);

		mvc.perform(
				post(REGISTER_URL)
						.content(asJsonString(model))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		// The default role after registration is RoleType.USER
		if (role != RoleType.USER) {
			User user = userRepository.findByUsername(username);
			user.setRoles(new ArrayList<>());
			user.getRoles().add(roleRepository.findByRoleType(role));
		}
	}

	protected ResultActions login(String username, String password) throws Exception {
		return mvc.perform(
				post(LOGIN_URL)
						.content(asJsonString(new RegisterUserBindingModel() {
							{
								setUsername(username);
								setPassword(password);
							}
						}))
						.contentType(MediaType.APPLICATION_JSON));
	}

	protected void getAsAdmin(String endpoint, ResultMatcher expectedResult) throws Exception {
		getRequest(endpoint, ADMIN_USER_USERNAME, ADMIN_USER_PASSWORD, expectedResult);
	}

	protected void getAsUser(String endpoint, ResultMatcher expectedResult) throws Exception {
		getRequest(endpoint, REG_USER_USERNAME, REG_USER_PASSWORD, expectedResult);
	}

	protected void getAsAnonymous(String endpoint, ResultMatcher expectedResult) throws Exception {
		getRequest(endpoint, "", "", expectedResult);
	}

	private void getRequest(String requestMapping, String username, String password, ResultMatcher expectedResult)
			throws Exception {
		RegisterUserBindingModel model = new RegisterUserBindingModel();
		model.setUsername(username);
		model.setPassword(password);

		String authorizationHeader = mvc.perform(
				post(LOGIN_URL)
						.content(asJsonString(model))
						.contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse()
				.getHeader(HEADER_STRING);

		mvc.perform(
				get(requestMapping)
						.header(HEADER_STRING, StringUtils.isEmpty(authorizationHeader) ? "" : authorizationHeader))
				.andExpect(expectedResult);
	}
}
