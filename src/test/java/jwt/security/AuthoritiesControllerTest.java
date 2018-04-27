package jwt.security;

import org.junit.Test;

import javax.transaction.Transactional;

import static jwt.security.web.AuthoritiesController.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthoritiesControllerTest extends BaseSecurityTestSuite {

	@Test
	@Transactional
	public void allAuthenticatedUsersCanCallNotRestrictedEndpoints() throws Exception {
		getAsAnonymous(REGISTERED_ONLY_ENDPOINT, status().is4xxClientError());
		getAsUser(REGISTERED_ONLY_ENDPOINT, status().isOk());
		getAsAdmin(REGISTERED_ONLY_ENDPOINT, status().isOk());
	}

	@Test
	@Transactional
	public void onlyUserCanCallUserEndpoints() throws Exception {
		getAsAnonymous(USER_ENDPOINT, status().is4xxClientError());
		getAsUser(USER_ENDPOINT, status().isOk());
		getAsAdmin(USER_ENDPOINT, status().is4xxClientError());
	}

	@Test
	@Transactional
	public void onlyAdminCanCallAdminEndpoints() throws Exception {
		getAsAnonymous(ADMIN_ENDPOINT, status().is4xxClientError());
		getAsUser(ADMIN_ENDPOINT, status().is4xxClientError());
		getAsAdmin(ADMIN_ENDPOINT, status().isOk());
	}
}
