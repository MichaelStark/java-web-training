package by.stark.sample.services.impl;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import by.stark.sample.datamodel.Picture;
import by.stark.sample.services.PictureService;

@Service
public class PictureServiceImpl extends AbstractServiceImpl<Long, Picture>
		implements PictureService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PictureServiceImpl.class);

	@Value(value = "${rootImagesFolfer}")
	private String rootFolder;

	@PostConstruct
	private void init() {
		// this method will be called by Spring after bean instantiation. Can be
		// used for any initialization process.
		LOGGER.info("Instance of PictureService is created. Class is: {}",
				getClass().getName());
	}

	public String getRootFolder() {
		return rootFolder;
	}

}
