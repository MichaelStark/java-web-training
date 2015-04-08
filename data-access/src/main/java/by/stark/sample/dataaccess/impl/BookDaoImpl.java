package by.stark.sample.dataaccess.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Repository;

import by.stark.sample.dataaccess.BookDao;
import by.stark.sample.datamodel.Author;
import by.stark.sample.datamodel.Book;
import by.stark.sample.datamodel.Book_;
import by.stark.sample.datamodel.Genre;

@Repository
public class BookDaoImpl extends AbstractDaoImpl<Long, Book> implements BookDao {

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
		criteria.distinct(true);
		criteria.where(cBuilder.isMember(author, root.get(Book_.authors)));
		return getEm().createQuery(criteria).getResultList();
	}

}