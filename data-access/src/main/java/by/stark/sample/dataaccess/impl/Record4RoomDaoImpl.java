package by.stark.sample.dataaccess.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import by.stark.sample.dataaccess.Record4RoomDao;
import by.stark.sample.datamodel.Libriary;
import by.stark.sample.datamodel.Record4Room;
import by.stark.sample.datamodel.Record4Room_;
import by.stark.sample.datamodel.Userprofile;
import by.stark.sample.datamodel.enums.RecordStatus;

@Repository
public class Record4RoomDaoImpl extends AbstractDaoImpl<Long, Record4Room>
		implements Record4RoomDao {

	protected Record4RoomDaoImpl() {
		super(Record4Room.class);
	}

	@Override
	public List<Record4Room> getAllRecordsByUser(Userprofile userprofile) {
		CriteriaBuilder cBuilder = getEm().getCriteriaBuilder();

		CriteriaQuery<Record4Room> root = cBuilder
				.createQuery(Record4Room.class);
		Root<Record4Room> criteria = root.from(Record4Room.class);

		root.select(criteria);

		root.where(cBuilder.equal(criteria.get(Record4Room_.userprofile),
				userprofile));

		TypedQuery<Record4Room> query = getEm().createQuery(root);
		List<Record4Room> results = query.getResultList();
		return results;
	}

	@Override
	public List<Record4Room> getAllRecordsByLibriary(Libriary libriary) {
		CriteriaBuilder cBuilder = getEm().getCriteriaBuilder();

		CriteriaQuery<Record4Room> root = cBuilder
				.createQuery(Record4Room.class);
		Root<Record4Room> criteria = root.from(Record4Room.class);

		root.select(criteria);

		root.where(cBuilder.equal(criteria.get(Record4Room_.libriary), libriary));

		TypedQuery<Record4Room> query = getEm().createQuery(root);
		List<Record4Room> results = query.getResultList();
		return results;
	}

	@Override
	public List<Record4Room> getAllRecordsByStatus(RecordStatus status) {
		CriteriaBuilder cBuilder = getEm().getCriteriaBuilder();

		CriteriaQuery<Record4Room> root = cBuilder
				.createQuery(Record4Room.class);
		Root<Record4Room> criteria = root.from(Record4Room.class);

		root.select(criteria);

		root.where(cBuilder.equal(criteria.get(Record4Room_.status), status));

		TypedQuery<Record4Room> query = getEm().createQuery(root);
		List<Record4Room> results = query.getResultList();
		return results;
	}

	@Override
	public List<Record4Room> getAllRecordsByTimeTake(Date timeTake) {
		CriteriaBuilder cBuilder = getEm().getCriteriaBuilder();

		CriteriaQuery<Record4Room> root = cBuilder
				.createQuery(Record4Room.class);
		Root<Record4Room> criteria = root.from(Record4Room.class);

		root.select(criteria);

		root.where(cBuilder.equal(criteria.get(Record4Room_.timeTake), timeTake));

		TypedQuery<Record4Room> query = getEm().createQuery(root);
		List<Record4Room> results = query.getResultList();
		return results;
	}

	@Override
	public List<Record4Room> getAllRecordsByTimeReturn(Date timeReturn) {
		CriteriaBuilder cBuilder = getEm().getCriteriaBuilder();

		CriteriaQuery<Record4Room> root = cBuilder
				.createQuery(Record4Room.class);
		Root<Record4Room> criteria = root.from(Record4Room.class);

		root.select(criteria);

		root.where(cBuilder.equal(criteria.get(Record4Room_.timeReturn),
				timeReturn));

		TypedQuery<Record4Room> query = getEm().createQuery(root);
		List<Record4Room> results = query.getResultList();
		return results;
	}
}