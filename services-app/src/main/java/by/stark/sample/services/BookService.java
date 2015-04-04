package by.stark.sample.services;

import java.util.List;

import by.stark.sample.datamodel.Author;
import by.stark.sample.datamodel.Book;
import by.stark.sample.datamodel.Genre;

public interface BookService extends AbstractService<Long, Book> {

	List<Book> getAllBooksByTitle(String title);

	List<Book> getAllBooksByAuthor(Author author);

	List<Book> getAllBooksByGenre(Genre genre);

}
