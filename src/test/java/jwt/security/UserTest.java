package jwt.security;

import jwt.security.config.SecurityConstants;
import jwt.security.entity.RoleType;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.util.NestedServletException;

import javax.transaction.Transactional;

import static jwt.security.config.SecurityConstants.HEADER_STRING;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserTest extends BaseSecurityTestSuite {

	@Test
	@Transactional
	public void successfullyRegisterUser() throws Exception {
		int usersCount = userRepository.findAll().size();
		register("pesho", "newPass", RoleType.USER);
		Assert.assertNotEquals(usersCount, userRepository.findAll().size());
	}

	@Test(expected = NestedServletException.class)
	@Transactional
	public void cannotRegisterWithAlreadyExistedUserName() throws Exception {
		register("qvkata", "new", RoleType.USER);
		register("qvkata", "new", RoleType.USER);
	}

	@Test
	@Transactional
	public void cannotLoginWithBadCredentials() throws Exception {

		login("NOT_VALID", "NOT_VALID")
				.andExpect(status().is4xxClientError());
	}

	@Test
	@Transactional
	public void loginWithRightCredentials() throws Exception {

		login(REG_USER_USERNAME, REG_USER_PASSWORD)
				.andExpect(status().isOk());
	}

	@Test
	@Transactional
	public void authorizationHeaderIsInTheResponseAfterLogin() throws Exception {

		String authorizationHeader = login(REG_USER_USERNAME, REG_USER_PASSWORD)
				.andReturn().getResponse().getHeader(HEADER_STRING);

		Assert.assertTrue(authorizationHeader.startsWith(SecurityConstants.TOKEN_PREFIX));
		Assert.assertTrue(authorizationHeader.length() > SecurityConstants.TOKEN_PREFIX.length());
	}
}

