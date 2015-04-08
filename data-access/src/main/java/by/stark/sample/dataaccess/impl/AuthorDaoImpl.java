package by.stark.sample.dataaccess.impl;

import org.springframework.stereotype.Repository;

import by.stark.sample.dataaccess.AuthorDao;
import by.stark.sample.datamodel.Author;

@Repository
public class AuthorDaoImpl extends AbstractDaoImpl<Long, Author> implements
		AuthorDao {

	protected AuthorDaoImpl() {
		super(Author.class);
	}

}