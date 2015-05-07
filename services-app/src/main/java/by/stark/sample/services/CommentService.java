package by.stark.sample.services;

import java.util.List;

import by.stark.sample.datamodel.Book;
import by.stark.sample.datamodel.Comment;

public interface CommentService extends AbstractService<Long, Comment> {

	List<Comment> getAllByBook(Book book);

	Comment getById(Long id);

}
