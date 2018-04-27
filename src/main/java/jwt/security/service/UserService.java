package jwt.security.service;

import jwt.security.entity.RoleType;
import jwt.security.entity.User;
import jwt.security.exception.UsernameAlreadyExist;
import jwt.security.model.RegisterUserBindingModel;
import jwt.security.repository.RoleRepository;
import jwt.security.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserService implements UserDetailsService {

	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public void register(RegisterUserBindingModel registerUserBindingModel) {

		User user = modelMapper.map(registerUserBindingModel, User.class);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.getRoles().add(roleRepository.findByRoleType(RoleType.USER));

		if (userRepository.findByUsername(user.getUsername()) != null) {
			throw new UsernameAlreadyExist("Username '" + user.getUsername() + "' already exist.");
		}

		userRepository.saveAndFlush(user);
		LOG.debug("User registered with username '{}'", user.getUsername());
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		if (StringUtils.isEmpty(username)) {
			throw new UsernameNotFoundException("Invalid username.");
		}

		User user = userRepository.findByUsername(username);

		if (user == null) {
			throw new UsernameNotFoundException("User with username '" + username + "' does not exist.");
		}

		return new org.springframework.security.core.userdetails.User(
				user.getUsername(),
				user.getPassword(),
				user.getAuthorities()
		);
	}
}