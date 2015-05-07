package by.stark.sample.services.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import by.stark.sample.dataaccess.CommentDao;
import by.stark.sample.datamodel.Book;
import by.stark.sample.datamodel.Comment;
import by.stark.sample.datamodel.Comment_;
import by.stark.sample.services.CommentService;

@Service
public class CommentServiceImpl extends AbstractServiceImpl<Long, Comment>
		implements CommentService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CommentServiceImpl.class);

	@Inject
	private CommentDao dao;

	@PostConstruct
	private void init() {
		// this method will be called by Spring after bean instantiation. Can be
		// used for any initialization process.
		LOGGER.info("Instance of CommentService is created. Class is: {}",
				getClass().getName());
	}

	@Override
	public List<Comment> getAllByBook(Book book) {
		return dao.getAllByFieldRestriction(Comment_.book, book, Comment_.book,
				Comment_.userprofile);
	}

	@Override
	public Comment getById(Long id) {
		return dao
				.getById(Comment_.id, id, Comment_.book, Comment_.userprofile);
	}
}
