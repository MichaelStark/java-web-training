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
import by.stark.sample.services.LibriaryService;

@Service
public class LibriaryServiceImpl implements LibriaryService {

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
	public Libriary get(Long id) {
		Libriary entity = dao.getById(id);
		return entity;
	}

	@Override
	public void saveOrUpdate(Libriary libriary) {
		if (libriary.getId() == null) {
			LOGGER.debug("Save new: {}", libriary);
			dao.insert(libriary);
		} else {
			LOGGER.debug("Update: {}", libriary);
			dao.update(libriary);
		}
	}

	@Override
	public void delete(Libriary libriary) {
		LOGGER.debug("Remove: {}", libriary);
		dao.delete(libriary.getId());
	}

	@Override
	public void deleteAll() {
		LOGGER.debug("Remove all libriarys");
		dao.deleteAll();
	}

	@Override
	public List<Libriary> getAllLibriarysByBook(Book book) {
		return dao.getAllLibriarysByBook(book);
	}

	@Override
	public List<Libriary> getAllAvailableLibriarys4HandsByBook(Book book) {
		return dao.getAllAvailableLibriarys4HandsByBook(book);
	}

	@Override
	public List<Libriary> getAllAvailableLibriarys4RoomByBook(Book book) {
		return dao.getAllAvailableLibriarys4RoomByBook(book);
	}

}
