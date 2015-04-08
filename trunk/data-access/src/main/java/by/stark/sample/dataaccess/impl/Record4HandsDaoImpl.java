package by.stark.sample.dataaccess.impl;

import org.springframework.stereotype.Repository;

import by.stark.sample.dataaccess.Record4HandsDao;
import by.stark.sample.datamodel.Record4Hands;

@Repository
public class Record4HandsDaoImpl extends AbstractDaoImpl<Long, Record4Hands>
		implements Record4HandsDao {

	protected Record4HandsDaoImpl() {
		super(Record4Hands.class);
	}

}