package by.stark.sample.services.impl;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import by.stark.sample.dataaccess.PublisherDao;
import by.stark.sample.datamodel.Publisher;
import by.stark.sample.services.PublisherService;

@Service
public class PublisherServiceImpl implements PublisherService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PublisherServiceImpl.class);

	@Inject
	private PublisherDao dao;

	@PostConstruct
	private void init() {
		// this method will be called by Spring after bean instantiation. Can be
		// used for any initialization process.
		LOGGER.info("Instance of PublisherService is created. Class is: {}",
				getClass().getName());
	}

	@Override
	public Publisher get(Long id) {
		Publisher entity = dao.getById(id);
		return entity;
	}

	@Override
	public void saveOrUpdate(Publisher publisher) {
		if (publisher.getId() == null) {
			LOGGER.debug("Save new: {}", publisher);
			dao.insert(publisher);
		} else {
			LOGGER.debug("Update: {}", publisher);
			dao.update(publisher);
		}
	}

	@Override
	public void delete(Publisher publisher) {
		LOGGER.debug("Remove: {}", publisher);
		dao.delete(publisher.getId());
	}

	@Override
	public void deleteAll() {
		LOGGER.debug("Remove all publishers");
		dao.deleteAll();
	}

}
