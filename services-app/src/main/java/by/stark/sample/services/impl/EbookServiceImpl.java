package by.stark.sample.services.impl;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import by.stark.sample.datamodel.Ebook;
import by.stark.sample.services.EbookService;

@Service
public class EbookServiceImpl extends AbstractServiceImpl<Long, Ebook>
		implements EbookService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(EbookServiceImpl.class);

	@PostConstruct
	private void init() {
		// this method will be called by Spring after bean instantiation. Can be
		// used for any initialization process.
		LOGGER.info("Instance of EbookService is created. Class is: {}",
				getClass().getName());
	}

}
