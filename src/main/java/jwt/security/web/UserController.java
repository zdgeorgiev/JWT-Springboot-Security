package jwt.security.web;

import jwt.security.model.RegisterUserBindingModel;
import jwt.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static jwt.security.config.SecurityConstants.REGISTER_URL;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@ResponseBody
	@RequestMapping(value = REGISTER_URL, method = RequestMethod.POST)
	public void register(@RequestBody RegisterUserBindingModel user) {
		userService.register(user);
	}
}
