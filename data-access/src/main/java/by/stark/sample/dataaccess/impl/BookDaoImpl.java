package by.stark.sample.dataaccess.impl;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import org.apache.commons.lang3.Validate;
import org.hibernate.jpa.criteria.OrderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import by.stark.sample.dataaccess.BookDao;
import by.stark.sample.datamodel.Author;
import by.stark.sample.datamodel.Book;
import by.stark.sample.datamodel.Book_;
import by.stark.sample.datamodel.Genre;

@Repository
public class BookDaoImpl extends AbstractDaoImpl<Long, Book> implements BookDao {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(BookDaoImpl.class);

	protected BookDaoImpl() {
		super(Book.class);
	}

	@Override
	public List<Book> getAllByGenre(Genre genre,
			SingularAttribute<Book, ?>... fetchAttributes) {
		Validate.notNull(genre, "Search attributes can't be empty. Attribute: "
				+ Book_.genres.getName());
		final CriteriaBuilder cBuilder = getEm().getCriteriaBuilder();
		final CriteriaQuery<Book> criteria = cBuilder.createQuery(Book.class);
		final Root<Book> root = criteria.from(Book.class);
		criteria.select(root);
		for (SingularAttribute<Book, ?> attr : fetchAttributes) {
			root.fetch(attr);
		}
		root.fetch(Book_.genres, JoinType.LEFT);
		root.fetch(Book_.authors, JoinType.LEFT);
		criteria.distinct(true);
		criteria.where(cBuilder.isMember(genre, root.get(Book_.genres)));
		return getEm().createQuery(criteria).getResultList();
	}

	@Override
	public List<Book> getAllByAuthor(Author author,
			SingularAttribute<Book, ?>... fetchAttributes) {
		Validate.notNull(
				author,
				"Search attributes can't be empty. Attribute: "
						+ Book_.authors.getName());
		final CriteriaBuilder cBuilder = getEm().getCriteriaBuilder();
		final CriteriaQuery<Book> criteria = cBuilder.createQuery(Book.class);
		final Root<Book> root = criteria.from(Book.class);
		criteria.select(root);
		for (SingularAttribute<Book, ?> attr : fetchAttributes) {
			root.fetch(attr);
		}
		root.fetch(Book_.authors, JoinType.LEFT);
		root.fetch(Book_.genres, JoinType.LEFT);
		criteria.distinct(true);
		criteria.where(cBuilder.isMember(author, root.get(Book_.authors)));
		return getEm().createQuery(criteria).getResultList();
	}

	@Override
	public List<Book> getAllByTitle(String title,
			SingularAttribute<Book, ?>... fetchAttributes) {
		Validate.notNull(title, "Search attributes can't be empty. Attribute: "
				+ Book_.title.getName());
		final CriteriaBuilder cBuilder = getEm().getCriteriaBuilder();
		final CriteriaQuery<Book> criteria = cBuilder.createQuery(Book.class);
		final Root<Book> root = criteria.from(Book.class);
		criteria.select(root);
		for (SingularAttribute<Book, ?> attr : fetchAttributes) {
			root.fetch(attr);
		}
		root.fetch(Book_.authors, JoinType.LEFT);
		root.fetch(Book_.genres, JoinType.LEFT);
		criteria.distinct(true);
		criteria.where(cBuilder.equal(root.get(Book_.title), title));
		return getEm().createQuery(criteria).getResultList();
	}

	@Override
	public List<Book> getAll(SingularAttribute<Book, ?>... fetchAttributes) {
		final CriteriaBuilder cBuilder = getEm().getCriteriaBuilder();
		final CriteriaQuery<Book> criteria = cBuilder.createQuery(Book.class);
		final Root<Book> root = criteria.from(Book.class);
		criteria.select(root);
		for (SingularAttribute<Book, ?> attr : fetchAttributes) {
			root.fetch(attr);
		}
		root.fetch(Book_.authors, JoinType.LEFT);
		root.fetch(Book_.genres, JoinType.LEFT);
		criteria.distinct(true);
		return getEm().createQuery(criteria).getResultList();
	}

	@Override
	public Book getById(Long id, SingularAttribute<Book, ?>... fetchAttributes) {
		Book result = null;
		Validate.notNull(id, "Search attributes can't be empty. Attribute: "
				+ Book_.id.getName());

		final CriteriaBuilder cBuilder = getEm().getCriteriaBuilder();
		final CriteriaQuery<Book> criteria = cBuilder.createQuery(Book.class);
		final Root<Book> root = criteria.from(Book.class);

		criteria.select(root);
		for (SingularAttribute<Book, ?> attribute : fetchAttributes) {
			root.fetch(attribute);
		}
		root.fetch(Book_.authors, JoinType.LEFT);
		root.fetch(Book_.genres, JoinType.LEFT);
		criteria.distinct(true);
		criteria.where(cBuilder.equal(root.get(Book_.id), id));

		try {
			result = getEm().createQuery(criteria).getSingleResult();
		} catch (NoResultException e) {
			LOGGER.debug("Search result is empty: {}", e);
			return null;
		} catch (NonUniqueResultException e) {
			LOGGER.warn(
					"Search result is more than one. !RETURN FIRST RESULT! Maybe you use this method not for ID UNIQUE field: {}",
					e);
			return getEm().createQuery(criteria).getResultList().get(0);
		}
		return result;
	}

	@Override
	public List<Book> getAllByTitleWithSortAndPagging(String title,
			int startRecord, int pageSize) {
		CriteriaBuilder cBuilder = getEm().getCriteriaBuilder();

		CriteriaQuery<Book> criteria = cBuilder.createQuery(Book.class);
		Root<Book> root = criteria.from(Book.class);

		criteria.select(root);

		criteria.where(cBuilder.like(cBuilder.lower(root.get(Book_.title)), "%"
				+ title.toLowerCase() + "%"));

		criteria.orderBy(new OrderImpl(root.get(Book_.title), true));
		TypedQuery<Book> query = getEm().createQuery(criteria);
		query.setFirstResult(startRecord);
		query.setMaxResults(pageSize);

		List<Book> results = query.getResultList();
		return results;
	}

	@Override
	public List<Book> getAllWithSortAndPagging(int startRecord, int pageSize) {
		CriteriaBuilder cBuilder = getEm().getCriteriaBuilder();

		CriteriaQuery<Book> criteria = cBuilder.createQuery(Book.class);
		Root<Book> root = criteria.from(Book.class);

		criteria.select(root);
		criteria.orderBy(new OrderImpl(root.get(Book_.id), false));
		TypedQuery<Book> query = getEm().createQuery(criteria);
		query.setFirstResult(startRecord);
		query.setMaxResults(pageSize);

		List<Book> results = query.getResultList();
		return results;
	}

	@Override
	public List<Book> getAllByGenreWithSortAndPagging(Genre genre,
			int startRecord, int pageSize) {
		CriteriaBuilder cBuilder = getEm().getCriteriaBuilder();

		CriteriaQuery<Book> criteria = cBuilder.createQuery(Book.class);
		Root<Book> root = criteria.from(Book.class);

		criteria.select(root);
		criteria.where(cBuilder.isMember(genre, root.get(Book_.genres)));
		criteria.orderBy(new OrderImpl(root.get(Book_.title), true));
		TypedQuery<Book> query = getEm().createQuery(criteria);
		query.setFirstResult(startRecord);
		query.setMaxResults(pageSize);

		List<Book> results = query.getResultList();
		return results;
	}

	@Override
	public List<Book> getAllByAuthorWithSortAndPagging(Author author,
			int startRecord, int pageSize) {
		CriteriaBuilder cBuilder = getEm().getCriteriaBuilder();

		CriteriaQuery<Book> criteria = cBuilder.createQuery(Book.class);
		Root<Book> root = criteria.from(Book.class);

		criteria.select(root);
		criteria.where(cBuilder.isMember(author, root.get(Book_.authors)));
		criteria.orderBy(new OrderImpl(root.get(Book_.title), true));
		TypedQuery<Book> query = getEm().createQuery(criteria);
		query.setFirstResult(startRecord);
		query.setMaxResults(pageSize);

		List<Book> results = query.getResultList();
		return results;
	}

	@Override
	public int getAllByTitleCount(String title) {
		CriteriaBuilder cBuilder = getEm().getCriteriaBuilder();

		CriteriaQuery<Book> criteria = cBuilder.createQuery(Book.class);
		Root<Book> root = criteria.from(Book.class);

		criteria.select(root);
		criteria.where(cBuilder.like(cBuilder.lower(root.get(Book_.title)), "%"
				+ title.toLowerCase() + "%"));

		TypedQuery<Book> query = getEm().createQuery(criteria);
		List<Book> result = query.getResultList();
		return result.size();
	}
}