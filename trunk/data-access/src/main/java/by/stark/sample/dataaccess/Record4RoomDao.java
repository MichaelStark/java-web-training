package by.stark.sample.dataaccess;

import java.util.Date;
import java.util.List;

import javax.persistence.metamodel.SingularAttribute;

import by.stark.sample.datamodel.Record4Room;

public interface Record4RoomDao extends AbstractDao<Long, Record4Room> {

	List<Record4Room> getAllByDateTake(final Date value, final Date nextValue,
			SingularAttribute<Record4Room, ?>... fetchAttributes);

	List<Record4Room> getAllByDateReturn(final Date value,
			final Date nextValue,
			SingularAttribute<Record4Room, ?>... fetchAttributes);

}
