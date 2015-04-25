package by.stark.sample.services;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import by.stark.sample.AbstractServiceTest;
import by.stark.sample.datamodel.Book;
import by.stark.sample.datamodel.Libriary;
import by.stark.sample.datamodel.Libriary_;

public class LibriaryServiceTest extends AbstractServiceTest {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(LibriaryServiceTest.class);

	@Inject
	private LibriaryService libriaryService;

	@Inject
	private PublisherService publisherService;

	@Inject
	private PictureService pictureService;

	@Inject
	private BookService bookService;

	@Before
	public void cleanUpData() {
		LOGGER.info("Instance of LibriaryService is injected. Class is: {}",
				libriaryService.getClass().getName());
		libriaryService.deleteAll();
	}

	@Test
	public void basicCRUDTest() {
		Libriary libriary = createLibriaryComplete();
		libriaryService.saveOrUpdate(libriary);

		Libriary libriaryFromDb = libriaryService.get(Libriary_.id,
				libriary.getId(), Libriary_.book);
		Assert.assertNotNull(libriaryFromDb);
		Assert.assertEquals(libriaryFromDb.getUin(), libriary.getUin());
		Assert.assertEquals(libriaryFromDb.getAvailability(),
				libriary.getAvailability());
		Assert.assertEquals(libriaryFromDb.getReadingRoom(),
				libriary.getReadingRoom());
		Assert.assertEquals(libriaryFromDb.getBook().getId(), libriary
				.getBook().getId());

		libriaryFromDb.setUin(randomLong());
		libriaryService.saveOrUpdate(libriaryFromDb);
		Libriary libriaryFromDbUpdated = libriaryService.get(libriary.getId());
		Assert.assertEquals(libriaryFromDb.getUin(),
				libriaryFromDbUpdated.getUin());
		Assert.assertNotEquals(libriaryFromDbUpdated.getUin(),
				libriary.getUin());

		libriaryService.delete(libriaryFromDbUpdated);
		Assert.assertNull(libriaryService.get(libriary.getId()));
	}

	@Test
	public void searchByBookTest() {
		Libriary libriary1 = createLibriaryComplete();
		Book book1 = libriary1.getBook();
		libriary1.setAvailability(true);
		libriary1.setReadingRoom(true);
		libriaryService.saveOrUpdate(libriary1);

		Libriary libriary2 = createLibriaryComplete();
		libriary2.setBook(book1);
		libriary2.setAvailability(true);
		libriary2.setReadingRoom(false);
		libriaryService.saveOrUpdate(libriary2);

		Libriary libriary3 = createLibriaryComplete();
		Book book2 = libriary3.getBook();
		libriary3.setAvailability(false);
		libriary3.setReadingRoom(true);
		libriaryService.saveOrUpdate(libriary3);

		Libriary libriary4 = createLibriaryComplete();
		libriary4.setBook(book1);
		libriary4.setAvailability(false);
		libriary4.setReadingRoom(false);
		libriaryService.saveOrUpdate(libriary4);

		List<Libriary> allLibriarys = libriaryService.getAll();
		Assert.assertEquals(allLibriarys.size(), 4);

		List<Libriary> allLibriarysByBook = libriaryService.getAllByField(
				Libriary_.book, book1);
		Assert.assertEquals(allLibriarysByBook.size(), 3);

		allLibriarysByBook = libriaryService.getAllByField(Libriary_.book,
				book2);
		Assert.assertEquals(allLibriarysByBook.size(), 1);
		Assert.assertEquals(allLibriarysByBook.get(0).getId(),
				libriary3.getId());

		allLibriarysByBook = libriaryService.getAllLibriarysByBook(book1, true,
				true);
		Assert.assertEquals(allLibriarysByBook.size(), 1);
		Assert.assertEquals(allLibriarysByBook.get(0).getId(),
				libriary1.getId());

		allLibriarysByBook = libriaryService.getAllLibriarysByBook(book1, true,
				false);
		Assert.assertEquals(allLibriarysByBook.size(), 1);
		Assert.assertEquals(allLibriarysByBook.get(0).getId(),
				libriary2.getId());

	}

	@Test
	public void uniqueConstraintsTest() {
		Libriary libriary = createLibriaryComplete();
		libriaryService.saveOrUpdate(libriary);

		Libriary duplicateLibriary = createLibriaryComplete();
		duplicateLibriary.setUin(libriary.getUin());
		try {
			libriaryService.saveOrUpdate(duplicateLibriary);
			Assert.fail("Not unique uin can't be saved.");
		} catch (PersistenceException e) {
			// expected
		}

		// should be saved now
		duplicateLibriary.setUin(randomLong());
		libriaryService.saveOrUpdate(duplicateLibriary);
	}

	@Test
	public void searchTest() {
		Libriary libriary = createLibriaryComplete();
		libriaryService.saveOrUpdate(libriary);

		List<Libriary> allLibriarys = libriaryService.getAll();
		Assert.assertEquals(allLibriarys.size(), 1);

	}

	@After
	public void finishTest() {
		libriaryService.deleteAll();
		Assert.assertEquals(libriaryService.getAll().size(), 0);
		bookService.deleteAll();
		Assert.assertEquals(bookService.getAll().size(), 0);
		publisherService.deleteAll();
		Assert.assertEquals(publisherService.getAll().size(), 0);
		pictureService.deleteAll();
		Assert.assertEquals(pictureService.getAll().size(), 0);
	}

	private Libriary createLibriaryComplete() {
		Libriary libriary = createLibriary();
		pictureService.saveOrUpdate(libriary.getBook().getPicture());
		publisherService.saveOrUpdate(libriary.getBook().getPublisher());
		bookService.saveOrUpdate(libriary.getBook());
		return libriary;
	}
}
