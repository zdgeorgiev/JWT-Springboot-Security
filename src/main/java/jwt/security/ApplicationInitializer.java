package jwt.security;

import jwt.security.entity.Role;
import jwt.security.entity.RoleType;
import jwt.security.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class ApplicationInitializer implements ApplicationRunner {

	private static final Logger LOG = LoggerFactory.getLogger(ApplicationInitializer.class);

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public void run(ApplicationArguments args) {

		if (roleRepository.count() == 0) {
			LOG.info("Initializing the roles..");
			Arrays.stream(RoleType.values()).forEach(role -> {
				LOG.info("Creating new role {}", role);
				roleRepository.save(new Role(role));
			});
		}
	}
}