package by.stark.sample.services.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import by.stark.sample.dataaccess.LibriaryDao;
import by.stark.sample.datamodel.Book;
import by.stark.sample.datamodel.Libriary;
import by.stark.sample.datamodel.Libriary_;
import by.stark.sample.services.LibriaryService;

@Service
public class LibriaryServiceImpl extends AbstractServiceImpl<Long, Libriary>
		implements LibriaryService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(LibriaryServiceImpl.class);

	@Inject
	private LibriaryDao dao;

	@PostConstruct
	private void init() {
		// this method will be called by Spring after bean instantiation. Can be
		// used for any initialization process.
		LOGGER.info("Instance of LibriaryService is created. Class is: {}",
				getClass().getName());
	}

	@Override
	public List<Libriary> getAllLibriarysByBook(Book book,
			Boolean availability, Boolean room) {
		return dao.getAllLibriarysByBook(book, availability, room);
	}

	@Override
	public List<Libriary> getAllByBook(Book book) {
		return dao.getAllByFieldRestriction(Libriary_.book, book);
	}

	@Override
	public Libriary getById(Long id) {
		return dao.getById(Libriary_.id, id, Libriary_.book);
	}

}
