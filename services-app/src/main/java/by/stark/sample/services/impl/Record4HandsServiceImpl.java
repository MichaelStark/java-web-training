package by.stark.sample.services.impl;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import by.stark.sample.datamodel.Record4Hands;
import by.stark.sample.services.Record4HandsService;

@Service
public class Record4HandsServiceImpl extends
		AbstractServiceImpl<Long, Record4Hands> implements Record4HandsService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(Record4HandsServiceImpl.class);

	@PostConstruct
	private void init() {
		// this method will be called by Spring after bean instantiation. Can be
		// used for any initialization process.
		LOGGER.info("Instance of Record4HandsService is created. Class is: {}",
				getClass().getName());
	}

}
