package by.stark.sample.services;

import java.util.List;

import by.stark.sample.datamodel.Author;
import by.stark.sample.datamodel.Book;
import by.stark.sample.datamodel.Genre;

public interface BookService extends AbstractService<Long, Book> {

	List<Book> getAllByAuthor(Author author);

	List<Book> getAllByGenre(Genre genre);

	List<Book> getAllByTitle(String title);

	@Override
	List<Book> getAll();

	Book getById(Long id);

	float getRating(Book book);

	List<Book> getAllByAuthorWithSortAndPagging(Author author, int startRecord,
			int pageSize);

	List<Book> getAllByGenreWithSortAndPagging(Genre genre, int startRecord,
			int pageSize);

	List<Book> getAllWithSortAndPagging(int startRecord, int pageSize);

	List<Book> getAllByTitleWithSortAndPagging(String title, int startRecord,
			int pageSize);

	int getAllByTitleCount(String title);

}
