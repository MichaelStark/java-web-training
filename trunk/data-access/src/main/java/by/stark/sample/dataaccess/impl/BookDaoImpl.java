package by.stark.sample.dataaccess.impl;

import org.springframework.stereotype.Repository;

import by.stark.sample.dataaccess.BookDao;
import by.stark.sample.datamodel.Book;

@Repository
public class BookDaoImpl extends AbstractDaoImpl<Long, Book> implements BookDao {

	protected BookDaoImpl() {
		super(Book.class);
	}
}