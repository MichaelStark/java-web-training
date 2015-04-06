package by.stark.sample.services;

import java.util.List;

import by.stark.sample.datamodel.Author;

public interface AuthorService extends AbstractService<Long, Author> {

	List<Author> getAll();

	List<Author> getAllAuthorsByFirstName(String firstName);

	List<Author> getAllAuthorsByLastName(String lastName);

}
