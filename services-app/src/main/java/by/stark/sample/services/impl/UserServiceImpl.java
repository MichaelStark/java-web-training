package by.stark.sample.services.impl;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import by.stark.sample.datamodel.Userprofile;
import by.stark.sample.services.UserService;

@Service
public class UserServiceImpl extends AbstractServiceImpl<Long, Userprofile>
		implements UserService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserServiceImpl.class);

	@PostConstruct
	private void init() {
		// this method will be called by Spring after bean instantiation. Can be
		// used for any initialization process.
		LOGGER.info("Instance of UserService is created. Class is: {}",
				getClass().getName());
	}

}
