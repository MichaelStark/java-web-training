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
import by.stark.sample.datamodel.Record4Hands;
import by.stark.sample.datamodel.Record4Hands_;

public class Record4HandsServiceTest extends AbstractServiceTest {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(Record4HandsServiceTest.class);

	@Inject
	private Record4HandsService recordService;

	@Inject
	private BookService bookService;

	@Inject
	private UserService userService;

	@Inject
	private LibriaryService libriaryService;

	@Inject
	private PublisherService publisherService;

	@Inject
	private PictureService pictureService;

	@Before
	public void cleanUpData() {
		LOGGER.info(
				"Instance of Record4HandsService is injected. Class is: {}",
				recordService.getClass().getName());
		recordService.deleteAll();
	}

	@Test
	public void basicCRUDTest() {
		Record4Hands record4Hands = createRecord4HandsComplete();
		recordService.saveOrUpdate(record4Hands);

		Record4Hands record4HandsFromDb = recordService.get(Record4Hands_.id,
				record4Hands.getId(), Record4Hands_.userprofile,
				Record4Hands_.libriary);
		Assert.assertNotNull(record4HandsFromDb);
		Assert.assertEquals(record4HandsFromDb.getStatus(),
				record4Hands.getStatus());
		Assert.assertEquals(record4HandsFromDb.getDescription(),
				record4Hands.getDescription());
		Assert.assertTrue(record4HandsFromDb.getDateTake().compareTo(
				record4Hands.getDateTake()) == 0);
		Assert.assertTrue(record4HandsFromDb.getDateReturn().compareTo(
				record4Hands.getDateReturn()) == 0);
		Assert.assertEquals(record4HandsFromDb.getUser().getId(), record4Hands
				.getUser().getId());
		Assert.assertEquals(record4HandsFromDb.getLibriary().getId(),
				record4Hands.getLibriary().getId());

		record4HandsFromDb.setDescription(randomString("description-"));
		recordService.saveOrUpdate(record4HandsFromDb);
		Record4Hands record4HandsFromDbUpdated = recordService.get(record4Hands
				.getId());
		Assert.assertEquals(record4HandsFromDb.getDescription(),
				record4HandsFromDbUpdated.getDescription());
		Assert.assertNotEquals(record4HandsFromDbUpdated.getDescription(),
				record4Hands.getDescription());

		recordService.delete(record4HandsFromDbUpdated);
		Assert.assertNull(recordService.get(record4Hands.getId()));
	}

	@Test
	public void searchByUserTest() {
		Record4Hands record4Hands = createRecord4HandsComplete();
		recordService.saveOrUpdate(record4Hands);

		Record4Hands anotherRecord4Hands = createRecord4HandsComplete();
		recordService.saveOrUpdate(anotherRecord4Hands);

		List<Record4Hands> allRecord4Hands = recordService.getAll();
		Assert.assertEquals(allRecord4Hands.size(), 2);

		List<Record4Hands> allRecord4HandsByTitle = recordService
				.getAllByField(Record4Hands_.userprofile,
						record4Hands.getUser());
		Assert.assertEquals(allRecord4HandsByTitle.size(), 1);
		Assert.assertEquals(allRecord4HandsByTitle.get(0).getId(),
				record4Hands.getId());

	}

	@Test
	public void searchByLibriaryTest() {
		Record4Hands record4Hands = createRecord4HandsComplete();
		recordService.saveOrUpdate(record4Hands);

		Record4Hands anotherRecord4Hands = createRecord4HandsComplete();
		recordService.saveOrUpdate(anotherRecord4Hands);

		List<Record4Hands> allRecord4Hands = recordService.getAll();
		Assert.assertEquals(allRecord4Hands.size(), 2);

		List<Record4Hands> allRecord4HandsByTitle = recordService
				.getAllByField(Record4Hands_.libriary,
						record4Hands.getLibriary());
		Assert.assertEquals(allRecord4HandsByTitle.size(), 1);
		Assert.assertEquals(allRecord4HandsByTitle.get(0).getId(),
				record4Hands.getId());

	}

	@Test
	public void searchByStatusTest() {
		Record4Hands record4Hands = createRecord4HandsComplete();
		recordService.saveOrUpdate(record4Hands);

		Record4Hands anotherRecord4Hands = createRecord4HandsComplete();
		recordService.saveOrUpdate(anotherRecord4Hands);

		List<Record4Hands> allRecord4Hands = recordService.getAll();
		Assert.assertEquals(allRecord4Hands.size(), 2);

		List<Record4Hands> allRecord4HandsByTitle = recordService
				.getAllByField(Record4Hands_.status, record4Hands.getStatus());
		if (record4Hands.getStatus() != anotherRecord4Hands.getStatus()) {
			Assert.assertEquals(allRecord4HandsByTitle.size(), 1);
			Assert.assertEquals(allRecord4HandsByTitle.get(0).getId(),
					record4Hands.getId());
		} else {
			Assert.assertEquals(allRecord4HandsByTitle.size(), 2);
		}

	}

	@Test
	public void searchByDateTest() {
		Record4Hands record4Hands = createRecord4HandsComplete();
		recordService.saveOrUpdate(record4Hands);

		Record4Hands anotherRecord4Hands = createRecord4HandsComplete();
		recordService.saveOrUpdate(anotherRecord4Hands);

		List<Record4Hands> allRecord4Hands = recordService.getAll();
		Assert.assertEquals(allRecord4Hands.size(), 2);

		List<Record4Hands> allRecord4HandsByTitle = recordService
				.getAllByField(Record4Hands_.dateTake,
						record4Hands.getDateTake());
		Assert.assertEquals(allRecord4HandsByTitle.size(), 1);
		Assert.assertEquals(allRecord4HandsByTitle.get(0).getId(),
				record4Hands.getId());

		allRecord4HandsByTitle = recordService.getAllByField(
				Record4Hands_.dateReturn, record4Hands.getDateReturn());
		Assert.assertEquals(allRecord4HandsByTitle.size(), 1);
		Assert.assertEquals(allRecord4HandsByTitle.get(0).getId(),
				record4Hands.getId());

	}

	@Test
	public void uniqueConstraintsTest() {
		Record4Hands record4Hands = createRecord4HandsComplete();
		recordService.saveOrUpdate(record4Hands);
		Record4Hands record4Hands2 = createRecord4HandsComplete();

		Record4Hands duplicateRecord4Hands = createRecord4HandsComplete();
		duplicateRecord4Hands.setLibriary(record4Hands.getLibriary());
		duplicateRecord4Hands.setDateTake(record4Hands.getDateTake());
		try {
			recordService.saveOrUpdate(duplicateRecord4Hands);
			Assert.fail("Not unique libriary with date_take can't be saved.");
		} catch (PersistenceException e) {
			// expected
		}
		// should be saved now
		duplicateRecord4Hands.setLibriary(record4Hands2.getLibriary());
		duplicateRecord4Hands.setDateTake(record4Hands2.getDateTake());
		recordService.saveOrUpdate(duplicateRecord4Hands);

		duplicateRecord4Hands.setLibriary(record4Hands.getLibriary());
		duplicateRecord4Hands.setDateReturn(record4Hands.getDateReturn());
		try {
			recordService.saveOrUpdate(duplicateRecord4Hands);
			Assert.fail("Not unique libriary with date_return can't be saved.");
		} catch (PersistenceException e) {
			// expected
		}

		// should be saved now
		duplicateRecord4Hands.setLibriary(record4Hands2.getLibriary());
		duplicateRecord4Hands.setDateReturn(record4Hands2.getDateReturn());
		recordService.saveOrUpdate(duplicateRecord4Hands);
	}

	@Test
	public void searchTest() {
		Record4Hands record4Hands = createRecord4HandsComplete();
		recordService.saveOrUpdate(record4Hands);

		List<Record4Hands> allRecord4Hands = recordService.getAll();
		Assert.assertEquals(allRecord4Hands.size(), 1);

	}

	@After
	public void finishTest() {
		recordService.deleteAll();
		Assert.assertEquals(recordService.getAll().size(), 0);
		libriaryService.deleteAll();
		Assert.assertEquals(libriaryService.getAll().size(), 0);
		bookService.deleteAll();
		Assert.assertEquals(bookService.getAll().size(), 0);
		userService.deleteAll();
		Assert.assertEquals(userService.getAll().size(), 0);
		publisherService.deleteAll();
		Assert.assertEquals(publisherService.getAll().size(), 0);
		pictureService.deleteAll();
		Assert.assertEquals(pictureService.getAll().size(), 0);
	}

	private Record4Hands createRecord4HandsComplete() {
		Record4Hands record4Hands = createRecord4Hands();
		pictureService.saveOrUpdate(record4Hands.getLibriary().getBook()
				.getPicture());
		publisherService.saveOrUpdate(record4Hands.getLibriary().getBook()
				.getPublisher());
		bookService.saveOrUpdate(record4Hands.getLibriary().getBook());
		libriaryService.saveOrUpdate(record4Hands.getLibriary());
		pictureService.saveOrUpdate(record4Hands.getUser().getPicture());
		userService.saveOrUpdate(record4Hands.getUser());
		return record4Hands;
	}

}
