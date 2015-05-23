package by.stark.sample.services.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import by.stark.sample.dataaccess.BookDao;
import by.stark.sample.dataaccess.CommentDao;
import by.stark.sample.datamodel.Author;
import by.stark.sample.datamodel.Book;
import by.stark.sample.datamodel.Book_;
import by.stark.sample.datamodel.Comment;
import by.stark.sample.datamodel.Comment_;
import by.stark.sample.datamodel.Genre;
import by.stark.sample.services.BookService;

@Service
public class BookServiceImpl extends AbstractServiceImpl<Long, Book> implements
		BookService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(BookServiceImpl.class);

	@Inject
	private BookDao dao;
	@Inject
	private CommentDao commentDao;

	@PostConstruct
	private void init() {
		// this method will be called by Spring after bean instantiation. Can be
		// used for any initialization process.
		LOGGER.info("Instance of BookService is created. Class is: {}",
				getClass().getName());
	}

	@Override
	public List<Book> getAllByAuthor(Author author) {
		return dao.getAllByAuthor(author, Book_.picture, Book_.publisher);
	}

	@Override
	public List<Book> getAllByGenre(Genre genre) {
		return dao.getAllByGenre(genre, Book_.picture, Book_.publisher);
	}

	@Override
	public List<Book> getAll() {
		return dao.getAll(Book_.picture, Book_.publisher);
	}

	@Override
	public Book getById(Long id) {
		return dao.getById(id, Book_.picture, Book_.publisher);
	}

	@Override
	public List<Book> getAllByTitle(String title) {
		return dao.getAllByTitle(title, Book_.picture, Book_.publisher);
	}

	// /
	@Override
	public List<Book> getAllByAuthorWithSortAndPagging(Author author,
			int startRecord, int pageSize) {
		return dao.getAllByAuthorWithSortAndPagging(author, startRecord,
				pageSize);
	}

	@Override
	public List<Book> getAllByGenreWithSortAndPagging(Genre genre,
			int startRecord, int pageSize) {
		return dao
				.getAllByGenreWithSortAndPagging(genre, startRecord, pageSize);
	}

	@Override
	public List<Book> getAllWithSortAndPagging(int startRecord, int pageSize) {
		return dao.getAllWithSortAndPagging(startRecord, pageSize);
	}

	@Override
	public List<Book> getAllByTitleWithSortAndPagging(String title,
			int startRecord, int pageSize) {
		title = title.trim();
		title = title.replaceAll("[\\s]{2,}", " ");
		return dao
				.getAllByTitleWithSortAndPagging(title, startRecord, pageSize);
	}

	@Override
	public float getRating(Book book) {
		float rating = 0;
		List<Comment> allComments = commentDao.getAllByFieldRestriction(
				Comment_.book, book);
		for (Comment comment : allComments) {
			rating += comment.getRating().ordinal();
		}
		float tmp = (rating / allComments.size()) * 10;
		return (float) (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) / 10;
	}

	@Override
	public int getAllByTitleCount(String title) {
		return dao.getAllByTitleCount(title);
	}
}
