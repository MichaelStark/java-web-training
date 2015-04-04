package by.stark.sample.services.impl;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import by.stark.sample.dataaccess.PictureDao;
import by.stark.sample.datamodel.Picture;
import by.stark.sample.services.PictureService;

@Service
public class PictureServiceImpl implements PictureService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PictureServiceImpl.class);

	@Inject
	private PictureDao dao;

	@PostConstruct
	private void init() {
		// this method will be called by Spring after bean instantiation. Can be
		// used for any initialization process.
		LOGGER.info("Instance of PictureService is created. Class is: {}",
				getClass().getName());
	}

	@Override
	public Picture get(Long id) {
		Picture entity = dao.getById(id);
		return entity;
	}

	@Override
	public void saveOrUpdate(Picture picture) {
		if (picture.getId() == null) {
			LOGGER.debug("Save new: {}", picture);
			dao.insert(picture);
		} else {
			LOGGER.debug("Update: {}", picture);
			dao.update(picture);
		}
	}

	@Override
	public void delete(Picture picture) {
		LOGGER.debug("Remove: {}", picture);
		dao.delete(picture.getId());
	}

	@Override
	public void deleteAll() {
		LOGGER.debug("Remove all pictures");
		dao.deleteAll();
	}

}
