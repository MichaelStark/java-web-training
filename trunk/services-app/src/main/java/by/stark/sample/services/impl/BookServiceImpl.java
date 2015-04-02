package by.stark.sample.services.impl;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import by.stark.sample.dataaccess.BookDao;
import by.stark.sample.datamodel.Book;
import by.stark.sample.services.BookService;

@Service
public class BookServiceImpl implements BookService {

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
	public Book get(Long id) {
		Book entity = dao.getById(id);
		return entity;
	}

	@Override
	public void saveOrUpdate(Book book) {
		if (book.getId() == null) {
			LOGGER.debug("Save new: {}", book);
			dao.insert(book);
		} else {
			LOGGER.debug("Update: {}", book);
			dao.update(book);
		}
	}

	@Override
	public void delete(Book book) {
		LOGGER.debug("Remove: {}", book);
		dao.delete(book.getId());
	}

	@Override
	public void deleteAll() {
		LOGGER.debug("Remove all books");
		dao.deleteAll();
	}

}
