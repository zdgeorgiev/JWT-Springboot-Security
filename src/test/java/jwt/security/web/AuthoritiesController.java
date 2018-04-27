package jwt.security.web;

import jwt.security.annotation.Admin;
import jwt.security.annotation.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthoritiesController {

	public static final String REGISTERED_ONLY_ENDPOINT = "/registered-only";
	public static final String ADMIN_ENDPOINT = "/admin";
	public static final String USER_ENDPOINT = "/user";

	@ResponseBody
	@RequestMapping(value = REGISTERED_ONLY_ENDPOINT, method = RequestMethod.GET)
	public String registered() {
		return "This is available for registered users!";
	}

	@User
	@ResponseBody
	@RequestMapping(value = USER_ENDPOINT, method = RequestMethod.GET)
	public String user() {
		return "This is available for user!";
	}

	@Admin
	@ResponseBody
	@RequestMapping(value = ADMIN_ENDPOINT, method = RequestMethod.GET)
	public String admin() {
		return "This is available for admin!";
	}
}

