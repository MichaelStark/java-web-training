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
import by.stark.sample.datamodel.Record4Room;
import by.stark.sample.datamodel.Record4Room_;

public class Record4RoomServiceTest extends AbstractServiceTest {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(Record4RoomServiceTest.class);

	@Inject
	private Record4RoomService recordService;

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
		LOGGER.info("Instance of Record4RoomService is injected. Class is: {}",
				recordService.getClass().getName());
		recordService.deleteAll();
	}

	@Test
	public void basicCRUDTest() {
		Record4Room record4Room = createRecord4RoomComplete();
		recordService.saveOrUpdate(record4Room);

		Record4Room record4RoomFromDb = recordService.get(Record4Room_.id,
				record4Room.getId(), Record4Room_.userprofile,
				Record4Room_.libriary);
		Assert.assertNotNull(record4RoomFromDb);
		Assert.assertEquals(record4RoomFromDb.getStatus(),
				record4Room.getStatus());
		Assert.assertEquals(record4RoomFromDb.getDescription(),
				record4Room.getDescription());
		Assert.assertTrue(record4RoomFromDb.getTimeTake().compareTo(
				record4Room.getTimeTake()) == 0);
		Assert.assertTrue(record4RoomFromDb.getTimeReturn().compareTo(
				record4Room.getTimeReturn()) == 0);
		Assert.assertEquals(record4RoomFromDb.getUser().getId(), record4Room
				.getUser().getId());
		Assert.assertEquals(record4RoomFromDb.getLibriary().getId(),
				record4Room.getLibriary().getId());

		record4RoomFromDb.setDescription(randomString("description-"));
		recordService.saveOrUpdate(record4RoomFromDb);
		Record4Room record4RoomFromDbUpdated = recordService.get(record4Room
				.getId());
		Assert.assertEquals(record4RoomFromDb.getDescription(),
				record4RoomFromDbUpdated.getDescription());
		Assert.assertNotEquals(record4RoomFromDbUpdated.getDescription(),
				record4Room.getDescription());

		recordService.delete(record4RoomFromDbUpdated);
		Assert.assertNull(recordService.get(record4Room.getId()));
	}

	@Test
	public void searchByUserTest() {
		Record4Room record4Room = createRecord4RoomComplete();
		recordService.saveOrUpdate(record4Room);

		Record4Room anotherRecord4Room = createRecord4RoomComplete();
		recordService.saveOrUpdate(anotherRecord4Room);

		List<Record4Room> allRecord4Room = recordService.getAll();
		Assert.assertEquals(allRecord4Room.size(), 2);

		List<Record4Room> allRecord4RoomByTitle = recordService.getAllByField(
				Record4Room_.userprofile, record4Room.getUser());
		Assert.assertEquals(allRecord4RoomByTitle.size(), 1);
		Assert.assertEquals(allRecord4RoomByTitle.get(0).getId(),
				record4Room.getId());

	}

	@Test
	public void searchByLibriaryTest() {
		Record4Room record4Room = createRecord4RoomComplete();
		recordService.saveOrUpdate(record4Room);

		Record4Room anotherRecord4Room = createRecord4RoomComplete();
		recordService.saveOrUpdate(anotherRecord4Room);

		List<Record4Room> allRecord4Room = recordService.getAll();
		Assert.assertEquals(allRecord4Room.size(), 2);

		List<Record4Room> allRecord4RoomByTitle = recordService.getAllByField(
				Record4Room_.libriary, record4Room.getLibriary());
		Assert.assertEquals(allRecord4RoomByTitle.size(), 1);
		Assert.assertEquals(allRecord4RoomByTitle.get(0).getId(),
				record4Room.getId());

	}

	@Test
	public void searchByStatusTest() {
		Record4Room record4Room = createRecord4RoomComplete();
		recordService.saveOrUpdate(record4Room);

		Record4Room anotherRecord4Room = createRecord4RoomComplete();
		recordService.saveOrUpdate(anotherRecord4Room);

		List<Record4Room> allRecord4Room = recordService.getAll();
		Assert.assertEquals(allRecord4Room.size(), 2);

		List<Record4Room> allRecord4RoomByTitle = recordService.getAllByField(
				Record4Room_.status, record4Room.getStatus());
		if (record4Room.getStatus() != anotherRecord4Room.getStatus()) {
			Assert.assertEquals(allRecord4RoomByTitle.size(), 1);
			Assert.assertEquals(allRecord4RoomByTitle.get(0).getId(),
					record4Room.getId());
		} else {
			Assert.assertEquals(allRecord4RoomByTitle.size(), 2);
		}

	}

	@Test
	public void searchByTimeTest() {
		Record4Room record4Room = createRecord4RoomComplete();
		recordService.saveOrUpdate(record4Room);

		Record4Room anotherRecord4Room = createRecord4RoomComplete();
		recordService.saveOrUpdate(anotherRecord4Room);

		List<Record4Room> allRecord4Room = recordService.getAll();
		Assert.assertEquals(allRecord4Room.size(), 2);

		List<Record4Room> allRecord4RoomByTitle = recordService.getAllByField(
				Record4Room_.timeTake, record4Room.getTimeTake());
		Assert.assertEquals(allRecord4RoomByTitle.size(), 1);
		Assert.assertEquals(allRecord4RoomByTitle.get(0).getId(),
				record4Room.getId());

		allRecord4RoomByTitle = recordService.getAllByField(
				Record4Room_.timeReturn, record4Room.getTimeReturn());
		Assert.assertEquals(allRecord4RoomByTitle.size(), 1);
		Assert.assertEquals(allRecord4RoomByTitle.get(0).getId(),
				record4Room.getId());

	}

	@Test
	public void uniqueConstraintsTest() {
		Record4Room record4Room = createRecord4RoomComplete();
		recordService.saveOrUpdate(record4Room);
		Record4Room record4Room2 = createRecord4RoomComplete();

		Record4Room duplicateRecord4Room = createRecord4RoomComplete();
		duplicateRecord4Room.setLibriary(record4Room.getLibriary());
		duplicateRecord4Room.setTimeTake(record4Room.getTimeTake());
		try {
			recordService.saveOrUpdate(duplicateRecord4Room);
			Assert.fail("Not unique libriary with date_take can't be saved.");
		} catch (PersistenceException e) {
			// expected
		}
		// should be saved now
		duplicateRecord4Room.setLibriary(record4Room2.getLibriary());
		duplicateRecord4Room.setTimeTake(record4Room2.getTimeTake());
		recordService.saveOrUpdate(duplicateRecord4Room);

		duplicateRecord4Room.setLibriary(record4Room.getLibriary());
		duplicateRecord4Room.setTimeReturn(record4Room.getTimeReturn());
		try {
			recordService.saveOrUpdate(duplicateRecord4Room);
			Assert.fail("Not unique libriary with date_return can't be saved.");
		} catch (PersistenceException e) {
			// expected
		}

		// should be saved now
		duplicateRecord4Room.setLibriary(record4Room2.getLibriary());
		duplicateRecord4Room.setTimeReturn(record4Room2.getTimeReturn());
		recordService.saveOrUpdate(duplicateRecord4Room);
	}

	@Test
	public void searchTest() {
		Record4Room record4Room = createRecord4RoomComplete();
		recordService.saveOrUpdate(record4Room);

		List<Record4Room> allRecord4Room = recordService.getAll();
		Assert.assertEquals(allRecord4Room.size(), 1);

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

	private Record4Room createRecord4RoomComplete() {
		Record4Room record4Room = createRecord4Room();
		pictureService.saveOrUpdate(record4Room.getLibriary().getBook()
				.getPicture());
		publisherService.saveOrUpdate(record4Room.getLibriary().getBook()
				.getPublisher());
		bookService.saveOrUpdate(record4Room.getLibriary().getBook());
		libriaryService.saveOrUpdate(record4Room.getLibriary());
		pictureService.saveOrUpdate(record4Room.getUser().getPicture());
		userService.saveOrUpdate(record4Room.getUser());
		return record4Room;
	}

}
