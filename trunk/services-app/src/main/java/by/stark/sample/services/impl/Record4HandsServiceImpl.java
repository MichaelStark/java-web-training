package by.stark.sample.services.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import by.stark.sample.dataaccess.Record4HandsDao;
import by.stark.sample.datamodel.Libriary;
import by.stark.sample.datamodel.Record4Hands;
import by.stark.sample.datamodel.User;
import by.stark.sample.datamodel.enums.RecordStatus;
import by.stark.sample.services.Record4HandsService;

@Service
public class Record4HandsServiceImpl implements Record4HandsService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(Record4HandsServiceImpl.class);

	@Inject
	private Record4HandsDao dao;

	@PostConstruct
	private void init() {
		// this method will be called by Spring after bean instantiation. Can be
		// used for any initialization process.
		LOGGER.info("Instance of Record4HandsService is created. Class is: {}",
				getClass().getName());
	}

	@Override
	public Record4Hands get(Long id) {
		Record4Hands entity = dao.getById(id);
		return entity;
	}

	@Override
	public void saveOrUpdate(Record4Hands record4hands) {
		if (record4hands.getId() == null) {
			LOGGER.debug("Save new: {}", record4hands);
			dao.insert(record4hands);
		} else {
			LOGGER.debug("Update: {}", record4hands);
			dao.update(record4hands);
		}
	}

	@Override
	public void delete(Record4Hands record4hands) {
		LOGGER.debug("Remove: {}", record4hands);
		dao.delete(record4hands.getId());
	}

	@Override
	public void deleteAll() {
		LOGGER.debug("Remove all record4hands");
		dao.deleteAll();
	}

	@Override
	public List<Record4Hands> getAllRecordsByUser(User user) {
		return dao.getAllRecordsByUser(user);
	}

	@Override
	public List<Record4Hands> getAllRecordsByLibriary(Libriary libriary) {
		return dao.getAllRecordsByLibriary(libriary);
	}

	@Override
	public List<Record4Hands> getAllRecordsByStatus(RecordStatus status) {
		return dao.getAllRecordsByStatus(status);
	}

	@Override
	public List<Record4Hands> getAllRecordsByDateTake(Date dateTake) {
		return dao.getAllRecordsByDateTake(dateTake);
	}

	@Override
	public List<Record4Hands> getAllRecordsByDateReturn(Date dateReturn) {
		return dao.getAllRecordsByDateReturn(dateReturn);
	}

}
