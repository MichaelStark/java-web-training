package by.stark.sample.services.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import by.stark.sample.dataaccess.BookDao;
import by.stark.sample.datamodel.Author;
import by.stark.sample.datamodel.Book;
import by.stark.sample.datamodel.Book_;
import by.stark.sample.datamodel.Genre;
import by.stark.sample.services.BookService;

@Service
public class BookServiceImpl extends AbstractServiceImpl<Long, Book> implements
		BookService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(BookServiceImpl.class);

	@Inject
	private BookDao dao;

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

}
