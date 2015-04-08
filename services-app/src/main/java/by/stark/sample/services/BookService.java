package by.stark.sample.services;

import java.util.List;

import javax.persistence.metamodel.SingularAttribute;

import by.stark.sample.datamodel.Author;
import by.stark.sample.datamodel.Book;
import by.stark.sample.datamodel.Genre;

public interface BookService extends AbstractService<Long, Book> {

	List<Book> getAllByAuthor(Author author,
			SingularAttribute<Book, ?>... fetchAttributes);

	List<Book> getAllByGenre(Genre genre,
			SingularAttribute<Book, ?>... fetchAttributes);

}
