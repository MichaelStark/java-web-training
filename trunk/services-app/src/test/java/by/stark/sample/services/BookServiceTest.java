package by.stark.sample.services;

import java.util.Iterator;

import javax.inject.Inject;

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

public class BookServiceTest extends AbstractServiceTest {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(BookServiceTest.class);

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
		Book book = createBook();
		pictureService.saveOrUpdate(book.getPicture());
		publisherService.saveOrUpdate(book.getPublisher());
		{
			Iterator<Author> it = book.getAuthor().iterator();

			while (it.hasNext()) {
				Author author = it.next();
				authorService.saveOrUpdate(author);
			}
		}
		{
			Iterator<Genre> it = book.getGenre().iterator();
			while (it.hasNext()) {
				Genre genre = it.next();
				genreService.saveOrUpdate(genre);
			}
		}
		bookService.saveOrUpdate(book);

		Book bookFromDb = bookService.get(book.getId());
		Assert.assertNotNull(bookFromDb);
		Assert.assertEquals(bookFromDb.getTitle(), book.getTitle());

		bookFromDb.setTitle("newTitle");
		bookService.saveOrUpdate(bookFromDb);
		Book bookFromDbUpdated = bookService.get(book.getId());
		Assert.assertEquals(bookFromDb.getTitle(), bookFromDbUpdated.getTitle());
		Assert.assertNotEquals(bookFromDbUpdated.getTitle(), book.getTitle());

		bookService.delete(bookFromDbUpdated);
		Assert.assertNull(bookService.get(book.getId()));
	}

	@After
	public void finishTest() {
		bookService.deleteAll();
		genreService.deleteAll();
		authorService.deleteAll();
		publisherService.deleteAll();
		pictureService.deleteAll();
		LOGGER.info("Finish BookServiceTest.  Class is: {}", this.getClass()
				.getName());
	}

}
