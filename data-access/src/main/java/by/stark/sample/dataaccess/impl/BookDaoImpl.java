package by.stark.sample.dataaccess.impl;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

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
	public List<Book> getAllBooksByTitle(String title) {
		CriteriaBuilder cBuilder = getEm().getCriteriaBuilder();

		CriteriaQuery<Book> root = cBuilder.createQuery(Book.class);
		Root<Book> criteria = root.from(Book.class);

		root.select(criteria);

		root.where(cBuilder.equal(criteria.get(Book_.title), title));

		TypedQuery<Book> query = getEm().createQuery(root);
		List<Book> results = query.getResultList();
		return results;
	}

	@Override
	public List<Book> getAllBooksByAuthor(Author author) {
		CriteriaBuilder cBuilder = getEm().getCriteriaBuilder();

		CriteriaQuery<Book> root = cBuilder.createQuery(Book.class);
		Root<Book> criteria = root.from(Book.class);

		root.select(criteria);

		root.where(cBuilder.equal(criteria.get(Book_.authors), author));

		TypedQuery<Book> query = getEm().createQuery(root);
		List<Book> results = query.getResultList();
		return results;
	}

	@Override
	public List<Book> getAllBooksByGenre(Genre genre) {
		CriteriaBuilder cBuilder = getEm().getCriteriaBuilder();

		CriteriaQuery<Book> root = cBuilder.createQuery(Book.class);
		Root<Book> criteria = root.from(Book.class);

		root.select(criteria);

		root.where(cBuilder.equal(criteria.get(Book_.genres), genre));

		TypedQuery<Book> query = getEm().createQuery(root);
		List<Book> results = query.getResultList();
		return results;
	}
}