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
import by.stark.sample.services.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

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
	public Comment get(Long id) {
		Comment entity = dao.getById(id);
		return entity;
	}

	@Override
	public void saveOrUpdate(Comment comment) {
		if (comment.getId() == null) {
			LOGGER.debug("Save new: {}", comment);
			dao.insert(comment);
		} else {
			LOGGER.debug("Update: {}", comment);
			dao.update(comment);
		}
	}

	@Override
	public void delete(Comment comment) {
		LOGGER.debug("Remove: {}", comment);
		dao.delete(comment.getId());
	}

	@Override
	public void deleteAll() {
		LOGGER.debug("Remove all comments");
		dao.deleteAll();
	}

	@Override
	public List<Comment> getAllCommentsByBook(Book book) {
		return dao.getAllCommentsByBook(book);
	}

}
