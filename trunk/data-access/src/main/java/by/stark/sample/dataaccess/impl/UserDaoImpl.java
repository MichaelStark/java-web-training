package by.stark.sample.dataaccess.impl;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import by.stark.sample.dataaccess.UserDao;
import by.stark.sample.datamodel.Userprofile;
import by.stark.sample.datamodel.Userprofile_;
import by.stark.sample.datamodel.enums.UserRole;
import by.stark.sample.datamodel.enums.UserStatus;

@Repository
public class UserDaoImpl extends AbstractDaoImpl<Long, Userprofile> implements
		UserDao {

	protected UserDaoImpl() {
		super(Userprofile.class);
	}

	@Override
	public List<Userprofile> getAllUsersByRole(UserRole role) {
		CriteriaBuilder cBuilder = getEm().getCriteriaBuilder();

		CriteriaQuery<Userprofile> root = cBuilder
				.createQuery(Userprofile.class);
		Root<Userprofile> criteria = root.from(Userprofile.class);

		root.select(criteria);

		root.where(cBuilder.equal(criteria.get(Userprofile_.role), role));

		TypedQuery<Userprofile> query = getEm().createQuery(root);
		List<Userprofile> results = query.getResultList();
		return results;
	}

	@Override
	public List<Userprofile> getAllUsersByStatus(UserStatus status) {
		CriteriaBuilder cBuilder = getEm().getCriteriaBuilder();

		CriteriaQuery<Userprofile> root = cBuilder
				.createQuery(Userprofile.class);
		Root<Userprofile> criteria = root.from(Userprofile.class);

		root.select(criteria);

		root.where(cBuilder.equal(criteria.get(Userprofile_.status), status));

		TypedQuery<Userprofile> query = getEm().createQuery(root);
		List<Userprofile> results = query.getResultList();
		return results;
	}
}
