package by.stark.sample.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import org.hibernate.LazyInitializationException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import by.stark.sample.AbstractServiceTest;
import by.stark.sample.datamodel.Author;
import by.stark.sample.datamodel.Book;
import by.stark.sample.datamodel.Genre;

public class Record4HandsServiceTest extends AbstractServiceTest {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(Record4HandsServiceTest.class);

	@Inject
	private BookService bookService;

	@Inject
	private GenreService genreService;

	@Inject
	private AuthorService authorService;

	@Inject
	private PublisherService publisherService;

	@Inject
	private PictureService pictureService;

	@Before
	public void cleanUpData() {
		LOGGER.info("Instance of BookService is injected. Class is: {}",
				bookService.getClass().getName());
		bookService.deleteAll();
	}

	@Test
	public void basicCRUDTest() {
		Book book = createBookComplete();
		bookService.saveOrUpdate(book);

		Book bookFromDb = bookService.get(book.getId());
		Assert.assertNotNull(bookFromDb);
		Assert.assertEquals(bookFromDb.getTitle(), book.getTitle());
		Assert.assertEquals(bookFromDb.getIsbn(), book.getIsbn());
		Assert.assertEquals(bookFromDb.getDescription(), book.getDescription());
		Assert.assertEquals(bookFromDb.getPages(), book.getPages());
		Assert.assertEquals(bookFromDb.getYear(), book.getYear());

		bookFromDb.setTitle("newTitle");
		bookService.saveOrUpdate(bookFromDb);
		Book bookFromDbUpdated = bookService.get(book.getId());
		Assert.assertEquals(bookFromDb.getTitle(), bookFromDbUpdated.getTitle());
		Assert.assertNotEquals(bookFromDbUpdated.getTitle(), book.getTitle());

		bookService.delete(bookFromDbUpdated);
		Assert.assertNull(bookService.get(book.getId()));
	}

	@Test
	public void manyToManyTest() {
		Book book = createBookComplete();

		int randomTestObjectsCount = randomTestObjectsCount();
		HashSet<Author> authors = new HashSet<Author>();
		HashSet<Genre> genres = new HashSet<Genre>();
		for (int i = 0; i < randomTestObjectsCount; i++) {
			Author author = createAuthor();
			authorService.saveOrUpdate(author);
			authors.add(author);

			Genre genre = createGenre();
			genreService.saveOrUpdate(genre);
			genres.add(genre);
		}
		book.setAuthors(authors);
		book.setGenres(genres);
		bookService.saveOrUpdate(book);

		Book bookFromDb = bookService.get(book.getId());
		try {
			// exception will be thrown because of lazy collection
			Set<Author> lazyAuthorsFromBook = bookFromDb.getAuthors();
			Set<Genre> lazyGenresFromBook = bookFromDb.getGenres();
		} catch (LazyInitializationException e) {
			// expected exception
		}
		bookFromDb.setAuthors(new HashSet<Author>());
		bookFromDb.setGenres(new HashSet<Genre>());
		bookService.saveOrUpdate(bookFromDb);
	}

	@Test
	public void searchByTitleTest() {
		Book book = createBookComplete();
		String title = book.getTitle();
		bookService.saveOrUpdate(book);

		Book anotherBook = createBookComplete();
		bookService.saveOrUpdate(anotherBook);

		List<Book> allBooks = bookService.getAll();
		Assert.assertEquals(allBooks.size(), 2);

		List<Book> allBooksByTitle = bookService.getAllBooksByTitle(title);
		Assert.assertEquals(allBooksByTitle.size(), 1);
		Assert.assertEquals(allBooksByTitle.get(0).getId(), book.getId());

	}

	// @Test
	public void searchByAuthorTest() {
		Book book = createBookComplete();
		Author author = createAuthor();
		Set<Author> authors = new HashSet<Author>();
		authors.add(author);
		book.setAuthors(authors);
		authorService.saveOrUpdate(author);
		bookService.saveOrUpdate(book);

		Book anotherBook = createBookComplete();
		bookService.saveOrUpdate(anotherBook);

		List<Book> allBooks = bookService.getAll();
		Assert.assertEquals(allBooks.size(), 2);

		List<Book> allBooksByAuthor = bookService.getAllBooksByAuthor(author);

		Assert.assertEquals(allBooksByAuthor.size(), 1);
		Assert.assertEquals(allBooksByAuthor.get(0).getId(), book.getId());

	}

	// @Test
	public void searchByGenreTest() {
		Book book = createBookComplete();
		Genre genre = createGenre();
		Set<Genre> genres = new HashSet<Genre>();
		genres.add(genre);
		book.setGenres(genres);
		genreService.saveOrUpdate(genre);
		bookService.saveOrUpdate(book);

		Book anotherBook = createBookComplete();
		bookService.saveOrUpdate(anotherBook);

		List<Book> allBooks = bookService.getAll();
		Assert.assertEquals(allBooks.size(), 2);

		List<Book> allBooksByGenre = bookService.getAllBooksByGenre(genre);

		Assert.assertEquals(allBooksByGenre.size(), 1);
		Assert.assertEquals(allBooksByGenre.get(0).getId(), book.getId());

	}

	@Test
	public void uniqueConstraintsTest() {
		Book book = createBookComplete();
		bookService.saveOrUpdate(book);

		Book duplicateBook = createBookComplete();
		duplicateBook.setIsbn(book.getIsbn());
		try {
			bookService.saveOrUpdate(duplicateBook);
			Assert.fail("Not unique isbn can't be saved.");
		} catch (PersistenceException e) {
			// expected
		}

		// should be saved now
		duplicateBook.setIsbn(randomString("isbn-"));
		bookService.saveOrUpdate(duplicateBook);
	}

	@Test
	public void searchTest() {
		Book book = createBookComplete();
		bookService.saveOrUpdate(book);

		List<Book> allBooks = bookService.getAll();
		Assert.assertEquals(allBooks.size(), 1);

	}

	@After
	public void finishTest() {
		bookService.deleteAll();
		Assert.assertEquals(bookService.getAll().size(), 0);
		genreService.deleteAll();
		Assert.assertEquals(genreService.getAll().size(), 0);
		authorService.deleteAll();
		Assert.assertEquals(authorService.getAll().size(), 0);
		publisherService.deleteAll();
		Assert.assertEquals(publisherService.getAll().size(), 0);
		pictureService.deleteAll();
		Assert.assertEquals(pictureService.getAll().size(), 0);
	}

	private Book createBookComplete() {
		Book book = createBook();
		pictureService.saveOrUpdate(book.getPicture());
		publisherService.saveOrUpdate(book.getPublisher());
		return book;
	}

}
