package by.stark.sample.services.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import by.stark.sample.dataaccess.AuthorDao;
import by.stark.sample.datamodel.Author;
import by.stark.sample.services.AuthorService;

@Service
public class AuthorServiceImpl implements AuthorService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AuthorServiceImpl.class);

	@Inject
	private AuthorDao dao;

	@PostConstruct
	private void init() {
		// this method will be called by Spring after bean instantiation. Can be
		// used for any initialization process.
		LOGGER.info("Instance of AuthorService is created. Class is: {}",
				getClass().getName());
	}

	@Override
	public Author get(Long id) {
		Author entity = dao.getById(id);
		return entity;
	}

	@Override
	public void saveOrUpdate(Author author) {
		if (author.getId() == null) {
			LOGGER.debug("Save new: {}", author);
			dao.insert(author);
		} else {
			LOGGER.debug("Update: {}", author);
			dao.update(author);
		}
	}

	@Override
	public void delete(Author author) {
		LOGGER.debug("Remove: {}", author);
		dao.delete(author.getId());
	}

	@Override
	public void deleteAll() {
		LOGGER.debug("Remove all authors");
		dao.deleteAll();
	}

	@Override
	public List<Author> getAllAuthorsByFirstName(String firstName) {
		return dao.getAllAuthorsByFirstName(firstName);
	}

	@Override
	public List<Author> getAllAuthorsByLastName(String lastName) {
		return dao.getAllAuthorsByLastName(lastName);
	}

	@Override
	public List<Author> getAll() {
		return dao.getAll();
	}

}
