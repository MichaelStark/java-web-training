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
import by.stark.sample.datamodel.Ebook;

public class EbookServiceTest extends AbstractServiceTest {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(EbookServiceTest.class);

	@Inject
	private BookService bookService;

	@Inject
	private EbookService ebookService;

	@Inject
	private PublisherService publisherService;

	@Inject
	private PictureService pictureService;

	@Before
	public void cleanUpData() {
		LOGGER.info("Instance of EbookService is injected. Class is: {}",
				ebookService.getClass().getName());
		ebookService.deleteAll();
	}

	@Test
	public void basicCRUDTest() {
		Ebook ebook = createEbookComplete();
		ebookService.saveOrUpdate(ebook);

		Ebook ebookFromDb = ebookService.getById(ebook.getId());
		Assert.assertNotNull(ebookFromDb);
		Assert.assertEquals(ebookFromDb.getName(), ebook.getName());
		Assert.assertEquals(ebookFromDb.getBook().getId(), ebook.getBook()
				.getId());

		ebookFromDb.setName("newName");
		ebookService.saveOrUpdate(ebookFromDb);
		Ebook ebookFromDbUpdated = ebookService.get(ebook.getId());
		Assert.assertEquals(ebookFromDb.getName(), ebookFromDbUpdated.getName());
		Assert.assertNotEquals(ebookFromDbUpdated.getName(), ebook.getName());

		ebookService.delete(ebookFromDbUpdated);
		Assert.assertNull(ebookService.get(ebook.getId()));
	}

	@Test
	public void searchByBookTest() {
		Ebook ebook = createEbookComplete();
		ebookService.saveOrUpdate(ebook);

		Ebook anotherEbook = createEbookComplete();
		ebookService.saveOrUpdate(anotherEbook);

		List<Ebook> allEbooks = ebookService.getAll();
		Assert.assertEquals(allEbooks.size(), 2);

		List<Ebook> allEbooksByBook = ebookService
				.getAllByBook(ebook.getBook());
		Assert.assertEquals(allEbooksByBook.size(), 1);
		Assert.assertEquals(allEbooksByBook.get(0).getId(), ebook.getId());

	}

	@Test
	public void uniqueConstraintsTest() {
		Ebook ebook = createEbookComplete();
		ebookService.saveOrUpdate(ebook);

		Ebook duplicateEbook = createEbookComplete();
		duplicateEbook.setName(ebook.getName());
		try {
			ebookService.saveOrUpdate(duplicateEbook);
			Assert.fail("Not unique name can't be saved.");
		} catch (PersistenceException e) {
			// expected
		}

		// should be saved now
		duplicateEbook.setName(randomString("name-"));
		ebookService.saveOrUpdate(duplicateEbook);
	}

	@Test
	public void searchTest() {
		Ebook ebook = createEbookComplete();
		ebookService.saveOrUpdate(ebook);

		List<Ebook> allEbooks = ebookService.getAll();
		Assert.assertEquals(allEbooks.size(), 1);

	}

	@After
	public void finishTest() {
		ebookService.deleteAll();
		Assert.assertEquals(ebookService.getAll().size(), 0);
		bookService.deleteAll();
		Assert.assertEquals(bookService.getAll().size(), 0);
		publisherService.deleteAll();
		Assert.assertEquals(publisherService.getAll().size(), 0);
		pictureService.deleteAll();
		Assert.assertEquals(pictureService.getAll().size(), 0);
	}

	private Ebook createEbookComplete() {
		Ebook ebook = createEbook();
		pictureService.saveOrUpdate(ebook.getBook().getPicture());
		publisherService.saveOrUpdate(ebook.getBook().getPublisher());
		bookService.saveOrUpdate(ebook.getBook());
		return ebook;
	}

}
