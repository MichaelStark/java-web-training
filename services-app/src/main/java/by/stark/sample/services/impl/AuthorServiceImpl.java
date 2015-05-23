package by.stark.sample.services.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import by.stark.sample.dataaccess.AuthorDao;
import by.stark.sample.datamodel.Author;
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
	public List<Author> getAllByName(String input) {

		input = input.trim();
		input = input.replaceAll("[\\s]{2,}", " ");

		List<Author> authorsList;

		int i = input.indexOf(" ", 1);
		if (i > 0) {
			String first = input.substring(0, i);
			String last = input.substring(i + 1);
			authorsList = dao.getAllByName(first, last, true);
			authorsList.addAll(dao.getAllByName(first, last, false));
		} else {
			authorsList = dao.getAllByName(input, true);
			authorsList.addAll(dao.getAllByName(input, false));
		}

		Set<Author> authors = new HashSet<Author>();
		authors.addAll(authorsList);
		authorsList.clear();
		authorsList.addAll(authors);
		return authorsList;
	}
}
