package by.stark.sample.services.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import by.stark.sample.dataaccess.Record4RoomDao;
import by.stark.sample.datamodel.Libriary;
import by.stark.sample.datamodel.Record4Room;
import by.stark.sample.datamodel.User;
import by.stark.sample.datamodel.enums.RecordStatus;
import by.stark.sample.services.Record4RoomService;

@Service
public class Record4RoomServiceImpl implements Record4RoomService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(Record4RoomServiceImpl.class);

	@Inject
	private Record4RoomDao dao;

	@PostConstruct
	private void init() {
		// this method will be called by Spring after bean instantiation. Can be
		// used for any initialization process.
		LOGGER.info("Instance of Record4RoomService is created. Class is: {}",
				getClass().getName());
	}

	@Override
	public Record4Room get(Long id) {
		Record4Room entity = dao.getById(id);
		return entity;
	}

	@Override
	public void saveOrUpdate(Record4Room record4room) {
		if (record4room.getId() == null) {
			LOGGER.debug("Save new: {}", record4room);
			dao.insert(record4room);
		} else {
			LOGGER.debug("Update: {}", record4room);
			dao.update(record4room);
		}
	}

	@Override
	public void delete(Record4Room record4room) {
		LOGGER.debug("Remove: {}", record4room);
		dao.delete(record4room.getId());
	}

	@Override
	public void deleteAll() {
		LOGGER.debug("Remove all record4rooms");
		dao.deleteAll();
	}

	@Override
	public List<Record4Room> getAllRecordsByUser(User user) {
		return dao.getAllRecordsByUser(user);
	}

	@Override
	public List<Record4Room> getAllRecordsByLibriary(Libriary libriary) {
		return dao.getAllRecordsByLibriary(libriary);
	}

	@Override
	public List<Record4Room> getAllRecordsByStatus(RecordStatus status) {
		return dao.getAllRecordsByStatus(status);
	}

	@Override
	public List<Record4Room> getAllRecordsByTimeTake(Date timeTake) {
		return dao.getAllRecordsByTimeTake(timeTake);
	}

	@Override
	public List<Record4Room> getAllRecordsByTimeReturn(Date timeReturn) {
		return dao.getAllRecordsByTimeReturn(timeReturn);
	}

}
