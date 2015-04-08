package by.stark.sample.services.impl;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import by.stark.sample.datamodel.Publisher;
import by.stark.sample.services.PublisherService;

@Service
public class PublisherServiceImpl extends AbstractServiceImpl<Long, Publisher>
		implements PublisherService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PublisherServiceImpl.class);

	@PostConstruct
	private void init() {
		// this method will be called by Spring after bean instantiation. Can be
		// used for any initialization process.
		LOGGER.info("Instance of PublisherService is created. Class is: {}",
				getClass().getName());
	}

}
