package by.stark.sample.services.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.metamodel.SingularAttribute;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import by.stark.sample.dataaccess.BookDao;
import by.stark.sample.datamodel.Author;
import by.stark.sample.datamodel.Book;
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
	public List<Book> getAllByAuthor(Author author,
			SingularAttribute<Book, ?>... fetchAttributes) {
		return dao.getAllByAuthor(author, fetchAttributes);
	}

	@Override
	public List<Book> getAllByGenre(Genre genre,
			SingularAttribute<Book, ?>... fetchAttributes) {
		return dao.getAllByGenre(genre, fetchAttributes);
	}

}
