package by.stark.sample.dataaccess.impl;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import by.stark.sample.dataaccess.UserDao;
import by.stark.sample.datamodel.User;
import by.stark.sample.datamodel.User_;
import by.stark.sample.datamodel.enums.UserRole;
import by.stark.sample.datamodel.enums.UserStatus;

@Repository
public class UserDaoImpl extends AbstractDaoImpl<Long, User> implements UserDao {

	protected UserDaoImpl() {
		super(User.class);
	}

	@Override
	public List<User> getAllUsersByRole(UserRole role) {
		CriteriaBuilder cBuilder = getEm().getCriteriaBuilder();

		CriteriaQuery<User> root = cBuilder.createQuery(User.class);
		Root<User> criteria = root.from(User.class);

		root.select(criteria);

		root.where(cBuilder.equal(criteria.get(User_.role), role));

		TypedQuery<User> query = getEm().createQuery(root);
		List<User> results = query.getResultList();
		return results;
	}

	@Override
	public List<User> getAllUsersByStatus(UserStatus status) {
		CriteriaBuilder cBuilder = getEm().getCriteriaBuilder();

		CriteriaQuery<User> root = cBuilder.createQuery(User.class);
		Root<User> criteria = root.from(User.class);

		root.select(criteria);

		root.where(cBuilder.equal(criteria.get(User_.status), status));

		TypedQuery<User> query = getEm().createQuery(root);
		List<User> results = query.getResultList();
		return results;
	}
}
