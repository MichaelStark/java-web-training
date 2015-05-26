package by.stark.sample.dataaccess.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import org.springframework.stereotype.Repository;

import by.stark.sample.dataaccess.Record4RoomDao;
import by.stark.sample.datamodel.Record4Room;
import by.stark.sample.datamodel.Record4Room_;

@Repository
public class Record4RoomDaoImpl extends AbstractDaoImpl<Long, Record4Room>
		implements Record4RoomDao {

	protected Record4RoomDaoImpl() {
		super(Record4Room.class);
	}

	@Override
	public List<Record4Room> getAllByDateTake(final Date value,
			final Date nextValue,
			SingularAttribute<Record4Room, ?>... fetchAttributes) {

		final CriteriaBuilder cBuilder = getEm().getCriteriaBuilder();
		final CriteriaQuery<Record4Room> criteria = cBuilder
				.createQuery(Record4Room.class);
		final Root<Record4Room> root = criteria.from(Record4Room.class);

		criteria.select(root);
		for (SingularAttribute<Record4Room, ?> attr : fetchAttributes) {
			root.fetch(attr);
		}
		criteria.distinct(true);
		criteria.where(cBuilder.and(cBuilder.greaterThanOrEqualTo(
				root.get(Record4Room_.timeTake), value), cBuilder.lessThan(
				root.get(Record4Room_.timeTake), nextValue)));
		return getEm().createQuery(criteria).getResultList();
	}

	@Override
	public List<Record4Room> getAllByDateReturn(final Date value,
			final Date nextValue,
			SingularAttribute<Record4Room, ?>... fetchAttributes) {

		final CriteriaBuilder cBuilder = getEm().getCriteriaBuilder();
		final CriteriaQuery<Record4Room> criteria = cBuilder
				.createQuery(Record4Room.class);
		final Root<Record4Room> root = criteria.from(Record4Room.class);

		criteria.select(root);
		for (SingularAttribute<Record4Room, ?> attr : fetchAttributes) {
			root.fetch(attr);
		}
		criteria.distinct(true);
		criteria.where(cBuilder.and(cBuilder.greaterThanOrEqualTo(
				root.get(Record4Room_.timeReturn), value), cBuilder.lessThan(
				root.get(Record4Room_.timeReturn), nextValue)));
		return getEm().createQuery(criteria).getResultList();
	}

}