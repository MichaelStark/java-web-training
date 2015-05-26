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
import by.stark.sample.datamodel.Record4Room_;
import by.stark.sample.datamodel.Userprofile;
import by.stark.sample.datamodel.enums.RecordStatus;
import by.stark.sample.services.Record4RoomService;

@Service
public class Record4RoomServiceImpl extends
		AbstractServiceImpl<Long, Record4Room> implements Record4RoomService {

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
	public List<Record4Room> getAllByUser(Userprofile user) {
		return dao.getAllByFieldRestriction(Record4Room_.userprofile, user,
				Record4Room_.libriary, Record4Room_.userprofile);
	}

	@Override
	public List<Record4Room> getAllByLibriary(Libriary libriary) {
		return dao.getAllByFieldRestriction(Record4Room_.libriary, libriary,
				Record4Room_.libriary, Record4Room_.userprofile);
	}

	@Override
	public List<Record4Room> getAllByStatus(RecordStatus status) {
		return dao.getAllByFieldRestriction(Record4Room_.status, status,
				Record4Room_.libriary, Record4Room_.userprofile);
	}

	@Override
	public List<Record4Room> getAllByTimeTake(Date date) {
		return dao.getAllByFieldRestriction(Record4Room_.timeTake, date,
				Record4Room_.libriary, Record4Room_.userprofile);
	}

	@Override
	public List<Record4Room> getAllByTimeReturn(Date date) {
		return dao.getAllByFieldRestriction(Record4Room_.timeReturn, date,
				Record4Room_.libriary, Record4Room_.userprofile);
	}

	@Override
	public Record4Room getById(Long id) {
		return dao.getById(Record4Room_.id, id, Record4Room_.libriary,
				Record4Room_.userprofile);
	}

	@Override
	public List<Record4Room> getAllByDateTake(Date date, Date nextDay) {
		return dao.getAllByDateTake(date, nextDay, Record4Room_.libriary,
				Record4Room_.userprofile);
	}

	@Override
	public List<Record4Room> getAllByDateReturn(Date date, Date nextDay) {
		return dao.getAllByDateReturn(date, nextDay, Record4Room_.libriary,
				Record4Room_.userprofile);
	}

}
