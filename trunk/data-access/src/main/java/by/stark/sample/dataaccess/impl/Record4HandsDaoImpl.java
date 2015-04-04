package by.stark.sample.dataaccess.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import by.stark.sample.dataaccess.Record4HandsDao;
import by.stark.sample.datamodel.Libriary;
import by.stark.sample.datamodel.Record4Hands;
import by.stark.sample.datamodel.Record4Hands_;
import by.stark.sample.datamodel.User;
import by.stark.sample.datamodel.enums.RecordStatus;

@Repository
public class Record4HandsDaoImpl extends AbstractDaoImpl<Long, Record4Hands>
		implements Record4HandsDao {

	protected Record4HandsDaoImpl() {
		super(Record4Hands.class);
	}

	@Override
	public List<Record4Hands> getAllRecordsByUser(User user) {
		CriteriaBuilder cBuilder = getEm().getCriteriaBuilder();

		CriteriaQuery<Record4Hands> root = cBuilder
				.createQuery(Record4Hands.class);
		Root<Record4Hands> criteria = root.from(Record4Hands.class);

		root.select(criteria);

		root.where(cBuilder.equal(criteria.get(Record4Hands_.user), user));

		TypedQuery<Record4Hands> query = getEm().createQuery(root);
		List<Record4Hands> results = query.getResultList();
		return results;
	}

	@Override
	public List<Record4Hands> getAllRecordsByLibriary(Libriary libriary) {
		CriteriaBuilder cBuilder = getEm().getCriteriaBuilder();

		CriteriaQuery<Record4Hands> root = cBuilder
				.createQuery(Record4Hands.class);
		Root<Record4Hands> criteria = root.from(Record4Hands.class);

		root.select(criteria);

		root.where(cBuilder.equal(criteria.get(Record4Hands_.libriary),
				libriary));

		TypedQuery<Record4Hands> query = getEm().createQuery(root);
		List<Record4Hands> results = query.getResultList();
		return results;
	}

	@Override
	public List<Record4Hands> getAllRecordsByStatus(RecordStatus status) {
		CriteriaBuilder cBuilder = getEm().getCriteriaBuilder();

		CriteriaQuery<Record4Hands> root = cBuilder
				.createQuery(Record4Hands.class);
		Root<Record4Hands> criteria = root.from(Record4Hands.class);

		root.select(criteria);

		root.where(cBuilder.equal(criteria.get(Record4Hands_.status), status));

		TypedQuery<Record4Hands> query = getEm().createQuery(root);
		List<Record4Hands> results = query.getResultList();
		return results;
	}

	@Override
	public List<Record4Hands> getAllRecordsByDateTake(Date dateTake) {
		CriteriaBuilder cBuilder = getEm().getCriteriaBuilder();

		CriteriaQuery<Record4Hands> root = cBuilder
				.createQuery(Record4Hands.class);
		Root<Record4Hands> criteria = root.from(Record4Hands.class);

		root.select(criteria);

		root.where(cBuilder.equal(criteria.get(Record4Hands_.dateTake),
				dateTake));

		TypedQuery<Record4Hands> query = getEm().createQuery(root);
		List<Record4Hands> results = query.getResultList();
		return results;
	}

	@Override
	public List<Record4Hands> getAllRecordsByDateReturn(Date dateReturn) {
		CriteriaBuilder cBuilder = getEm().getCriteriaBuilder();

		CriteriaQuery<Record4Hands> root = cBuilder
				.createQuery(Record4Hands.class);
		Root<Record4Hands> criteria = root.from(Record4Hands.class);

		root.select(criteria);

		root.where(cBuilder.equal(criteria.get(Record4Hands_.dateReturn),
				dateReturn));

		TypedQuery<Record4Hands> query = getEm().createQuery(root);
		List<Record4Hands> results = query.getResultList();
		return results;
	}
}