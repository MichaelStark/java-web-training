package by.stark.sample.dataaccess.impl;

import org.springframework.stereotype.Repository;

import by.stark.sample.dataaccess.CommentDao;
import by.stark.sample.datamodel.Comment;

@Repository
public class CommentDaoImpl extends AbstractDaoImpl<Long, Comment> implements
		CommentDao {

	protected CommentDaoImpl() {
		super(Comment.class);
	}
}