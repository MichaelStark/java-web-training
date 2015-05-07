package by.stark.sample.services.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import by.stark.sample.dataaccess.AuthorDao;
import by.stark.sample.datamodel.Author;
import by.stark.sample.datamodel.Author_;
import by.stark.sample.services.AuthorService;

@Service
public class AuthorServiceImpl extends AbstractServiceImpl<Long, Author>
		implements AuthorService {

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
	public List<Author> getAllByName(String name) {
		List<Author> authorsByFirstName = dao.getAllByFieldRestriction(
				Author_.firstName, name);
		List<Author> authorsByLastName = dao.getAllByFieldRestriction(
				Author_.lastName, name);

		if (authorsByFirstName.size() == 0) {
			if (authorsByLastName.size() == 0) {
				return null;
			}
		} else {
			if (authorsByLastName.size() == 0) {
				return authorsByFirstName;
			} else {
				authorsByLastName.addAll(authorsByFirstName);
			}
		}
		return authorsByLastName;
	}

	@Override
	public List<Author> getAllByName(String firstName, String lastName) {
		List<Author> author = dao.getAllByName(firstName, lastName);
		author.addAll(dao.getAllByName(lastName, firstName));
		return author;
	}
}
