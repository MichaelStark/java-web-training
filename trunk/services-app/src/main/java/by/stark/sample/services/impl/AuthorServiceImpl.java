package by.stark.sample.services.impl;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import by.stark.sample.datamodel.Author;
import by.stark.sample.services.AuthorService;

@Service
public class AuthorServiceImpl extends AbstractServiceImpl<Long, Author>
		implements AuthorService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AuthorServiceImpl.class);

	@PostConstruct
	private void init() {
		// this method will be called by Spring after bean instantiation. Can be
		// used for any initialization process.
		LOGGER.info("Instance of AuthorService is created. Class is: {}",
				getClass().getName());
	}
}
