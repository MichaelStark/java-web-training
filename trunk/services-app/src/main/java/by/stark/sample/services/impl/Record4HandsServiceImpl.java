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
import by.stark.sample.datamodel.Record4Hands_;
import by.stark.sample.datamodel.Userprofile;
import by.stark.sample.datamodel.enums.RecordStatus;
import by.stark.sample.services.Record4HandsService;

@Service
public class Record4HandsServiceImpl extends
		AbstractServiceImpl<Long, Record4Hands> implements Record4HandsService {

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
	public List<Record4Hands> getAllByUser(Userprofile user) {
		return dao.getAllByFieldRestriction(Record4Hands_.userprofile, user,
				Record4Hands_.libriary, Record4Hands_.userprofile);
	}

	@Override
	public List<Record4Hands> getAllByLibriary(Libriary libriary) {
		return dao.getAllByFieldRestriction(Record4Hands_.libriary, libriary,
				Record4Hands_.libriary, Record4Hands_.userprofile);
	}

	@Override
	public List<Record4Hands> getAllByStatus(RecordStatus status) {
		return dao.getAllByFieldRestriction(Record4Hands_.status, status,
				Record4Hands_.libriary, Record4Hands_.userprofile);
	}

	@Override
	public List<Record4Hands> getAllByDateTake(Date date) {
		return dao.getAllByFieldRestriction(Record4Hands_.dateTake, date,
				Record4Hands_.libriary, Record4Hands_.userprofile);
	}

	@Override
	public List<Record4Hands> getAllByDateReturn(Date date) {
		return dao.getAllByFieldRestriction(Record4Hands_.dateReturn, date,
				Record4Hands_.libriary, Record4Hands_.userprofile);
	}

	@Override
	public Record4Hands getById(Long id) {
		return dao.getById(Record4Hands_.id, id, Record4Hands_.libriary,
				Record4Hands_.userprofile);
	}

}
