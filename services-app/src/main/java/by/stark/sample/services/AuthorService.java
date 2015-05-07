package by.stark.sample.services;

import java.util.List;

import by.stark.sample.datamodel.Author;

public interface AuthorService extends AbstractService<Long, Author> {

	List<Author> getAllByName(String name);

	List<Author> getAllByName(String firstName, String lastName);

}
