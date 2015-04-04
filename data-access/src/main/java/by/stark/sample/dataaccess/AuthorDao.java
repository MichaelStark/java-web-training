package by.stark.sample.dataaccess;

import java.util.List;

import by.stark.sample.datamodel.Author;

public interface AuthorDao extends AbstractDao<Long, Author> {

	List<Author> getAllAuthorsByFirstName(String firstName);

	List<Author> getAllAuthorsByLastName(String lastName);

}
