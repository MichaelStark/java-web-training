package by.stark.sample.dataaccess;

import java.util.List;

import javax.persistence.metamodel.SingularAttribute;

import by.stark.sample.datamodel.Author;
import by.stark.sample.datamodel.Book;
import by.stark.sample.datamodel.Genre;

public interface BookDao extends AbstractDao<Long, Book> {

	List<Book> getAllByAuthor(Author author,
			SingularAttribute<Book, ?>... fetchAttributes);

	List<Book> getAllByGenre(Genre genre,
			SingularAttribute<Book, ?>... fetchAttributes);

}
