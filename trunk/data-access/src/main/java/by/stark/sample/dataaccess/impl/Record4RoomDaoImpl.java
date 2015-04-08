package by.stark.sample.dataaccess.impl;

import org.springframework.stereotype.Repository;

import by.stark.sample.dataaccess.Record4RoomDao;
import by.stark.sample.datamodel.Record4Room;

@Repository
public class Record4RoomDaoImpl extends AbstractDaoImpl<Long, Record4Room>
		implements Record4RoomDao {

	protected Record4RoomDaoImpl() {
		super(Record4Room.class);
	}

}