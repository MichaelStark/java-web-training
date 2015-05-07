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
import by.stark.sample.datamodel.Ebook_;
import by.stark.sample.services.EbookService;

@Service
public class EbookServiceImpl extends AbstractServiceImpl<Long, Ebook>
		implements EbookService {

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
	public List<Ebook> getAllByBook(Book book) {
		return dao.getAllByFieldRestriction(Ebook_.book, book, Ebook_.book);
	}

	@Override
	public Ebook getById(Long id) {
		return dao.getById(Ebook_.id, id, Ebook_.book);
	}

}
