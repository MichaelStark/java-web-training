package by.stark.sample.dataaccess.impl;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

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
	public List<Author> getAllByName(String firstName, String lastName) {
		CriteriaBuilder cBuilder = getEm().getCriteriaBuilder();

		CriteriaQuery<Author> root = cBuilder.createQuery(Author.class);
		Root<Author> criteria = root.from(Author.class);

		root.select(criteria);

		root.where(cBuilder.and(
				cBuilder.equal(criteria.get(Author_.firstName), firstName),
				cBuilder.equal(criteria.get(Author_.lastName), lastName)));

		TypedQuery<Author> query = getEm().createQuery(root);
		List<Author> results = query.getResultList();
		return results;
	}

}