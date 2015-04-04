package by.stark.sample.dataaccess.impl;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import by.stark.sample.dataaccess.CommentDao;
import by.stark.sample.datamodel.Book;
import by.stark.sample.datamodel.Comment;
import by.stark.sample.datamodel.Comment_;

@Repository
public class CommentDaoImpl extends AbstractDaoImpl<Long, Comment> implements
		CommentDao {

	protected CommentDaoImpl() {
		super(Comment.class);
	}

	@Override
	public List<Comment> getAllCommentsByBook(Book book) {
		CriteriaBuilder cBuilder = getEm().getCriteriaBuilder();

		CriteriaQuery<Comment> root = cBuilder.createQuery(Comment.class);
		Root<Comment> criteria = root.from(Comment.class);

		root.select(criteria);

		root.where(cBuilder.equal(criteria.get(Comment_.book), book));

		TypedQuery<Comment> query = getEm().createQuery(root);
		List<Comment> results = query.getResultList();
		return results;
	}
}