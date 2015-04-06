package by.stark.sample.services.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import by.stark.sample.dataaccess.EbookDao;
import by.stark.sample.datamodel.Book;
import by.stark.sample.datamodel.Ebook;
import by.stark.sample.services.EbookService;

@Service
public class EbookServiceImpl implements EbookService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(EbookServiceImpl.class);

	@Inject
	private EbookDao dao;

	@PostConstruct
	private void init() {
		// this method will be called by Spring after bean instantiation. Can be
		// used for any initialization process.
		LOGGER.info("Instance of EbookService is created. Class is: {}",
				getClass().getName());
	}

	@Override
	public Ebook get(Long id) {
		Ebook entity = dao.getById(id);
		return entity;
	}

	@Override
	public void saveOrUpdate(Ebook ebook) {
		if (ebook.getId() == null) {
			LOGGER.debug("Save new: {}", ebook);
			dao.insert(ebook);
		} else {
			LOGGER.debug("Update: {}", ebook);
			dao.update(ebook);
		}
	}

	@Override
	public void delete(Ebook ebook) {
		LOGGER.debug("Remove: {}", ebook);
		dao.delete(ebook.getId());
	}

	@Override
	public void deleteAll() {
		LOGGER.debug("Remove all ebooks");
		dao.deleteAll();
	}

	@Override
	public List<Ebook> getAllEbooksByBook(Book book) {
		return dao.getAllEbooksByBook(book);
	}

	@Override
	public List<Ebook> getAll() {
		return dao.getAll();
	}

}
