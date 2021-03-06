package by.stark.sample.services.impl;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import by.stark.sample.datamodel.Genre;
import by.stark.sample.services.GenreService;

@Service
public class GenreServiceImpl extends AbstractServiceImpl<Long, Genre>
		implements GenreService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(GenreServiceImpl.class);

	@PostConstruct
	private void init() {
		// this method will be called by Spring after bean instantiation. Can be
		// used for any initialization process.
		LOGGER.info("Instance of GenreService is created. Class is: {}",
				getClass().getName());
	}

}
