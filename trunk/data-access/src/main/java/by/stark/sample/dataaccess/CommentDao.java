package by.stark.sample.dataaccess;

import java.util.List;

import by.stark.sample.datamodel.Book;
import by.stark.sample.datamodel.Comment;

public interface CommentDao extends AbstractDao<Long, Comment> {

	List<Comment> getAllCommentsByBook(Book book);

}
