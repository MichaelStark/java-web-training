package by.stark.sample.services;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import by.stark.sample.AbstractServiceTest;
import by.stark.sample.datamodel.Book;

public class BookServiceTest extends AbstractServiceTest {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(BookServiceTest.class);

	@Inject
	private BookService bookService;

	@Before
	public void cleanUpData() {
		LOGGER.info("Instance of BookService is injected. Class is: {}",
				bookService.getClass().getName());
		bookService.deleteAll();
	}

	@Test
	public void basicCRUDTest() {
		Book book = createBook();
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

	private Book createBook() {
		Book book = new Book();
		book.setDescription(randomString("description-"));
		book.setIsbn(randomString("isbn-"));
		book.setPages(randomLong());
		book.setTitle(randomString("title-"));
		book.setYear(randomLong());
		return book;
	}

}
