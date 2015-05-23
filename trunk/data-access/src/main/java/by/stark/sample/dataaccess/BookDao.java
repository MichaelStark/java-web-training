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

	List<Book> getAllByTitle(String title,
			SingularAttribute<Book, ?>... fetchAttributes);

	@Override
	List<Book> getAll(SingularAttribute<Book, ?>... fetchAttributes);

	Book getById(Long id, SingularAttribute<Book, ?>... fetchAttributes);

	List<Book> getAllByTitleWithSortAndPagging(String title, int startRecord,
			int pageSize);

	List<Book> getAllByGenreWithSortAndPagging(Genre genre, int startRecord,
			int pageSize);

	List<Book> getAllByAuthorWithSortAndPagging(Author author, int startRecord,
			int pageSize);

	List<Book> getAllWithSortAndPagging(int startRecord, int pageSize);

	int getAllByTitleCount(String title);

}
