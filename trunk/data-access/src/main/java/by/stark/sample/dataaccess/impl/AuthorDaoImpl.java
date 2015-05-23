package by.stark.sample.dataaccess.impl;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.jpa.criteria.OrderImpl;
import org.springframework.stereotype.Repository;

import by.stark.sample.dataaccess.AuthorDao;
import by.stark.sample.datamodel.Author;
import by.stark.sample.datamodel.Author_;

@Repository
public class AuthorDaoImpl extends AbstractDaoImpl<Long, Author> implements
		AuthorDao {

	protected AuthorDaoImpl() {
		super(Author.class);
	}

	@Override
	public List<Author> getAllByName(String name, boolean byFirstName) {
		CriteriaBuilder cBuilder = getEm().getCriteriaBuilder();

		CriteriaQuery<Author> root = cBuilder.createQuery(Author.class);
		Root<Author> criteria = root.from(Author.class);

		root.select(criteria);

		if (byFirstName) {
			root.where(cBuilder.like(
					cBuilder.lower(criteria.get(Author_.firstName)),
					"%" + name.toLowerCase() + "%"));
		} else {
			root.where(cBuilder.like(
					cBuilder.lower(criteria.get(Author_.lastName)),
					"%" + name.toLowerCase() + "%"));
		}

		root.orderBy(new OrderImpl(criteria.get(Author_.lastName), true));

		TypedQuery<Author> query = getEm().createQuery(root);
		List<Author> results = query.getResultList();
		return results;
	}

	@Override
	public List<Author> getAllByName(String name1, String name2,
			boolean byFirstName) {
		CriteriaBuilder cBuilder = getEm().getCriteriaBuilder();

		CriteriaQuery<Author> root = cBuilder.createQuery(Author.class);
		Root<Author> criteria = root.from(Author.class);

		root.select(criteria);

		if (byFirstName) {
			root.where(cBuilder.and(cBuilder.like(
					cBuilder.lower(criteria.get(Author_.lastName)),
					"%" + name2.toLowerCase() + "%")), cBuilder.like(
					cBuilder.lower(criteria.get(Author_.firstName)),
					name1.toLowerCase()));
		} else {
			root.where(cBuilder.and(cBuilder.like(
					cBuilder.lower(criteria.get(Author_.firstName)), "%"
							+ name2.toLowerCase() + "%")), cBuilder.like(
					cBuilder.lower(criteria.get(Author_.lastName)),
					name1.toLowerCase()));
		}

		root.orderBy(new OrderImpl(criteria.get(Author_.lastName), true));

		TypedQuery<Author> query = getEm().createQuery(root);
		List<Author> results = query.getResultList();
		return results;
	}
}