package by.stark.sample.services.impl;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import by.stark.sample.datamodel.Record4Room;
import by.stark.sample.services.Record4RoomService;

@Service
public class Record4RoomServiceImpl extends
		AbstractServiceImpl<Long, Record4Room> implements Record4RoomService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(Record4RoomServiceImpl.class);

	@PostConstruct
	private void init() {
		// this method will be called by Spring after bean instantiation. Can be
		// used for any initialization process.
		LOGGER.info("Instance of Record4RoomService is created. Class is: {}",
				getClass().getName());
	}

}
