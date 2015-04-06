package by.stark.sample.services;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import by.stark.sample.AbstractServiceTest;
import by.stark.sample.datamodel.Publisher;

public class PublisherServiceTest extends AbstractServiceTest {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PublisherServiceTest.class);

	@Inject
	private PublisherService publisherService;

	@Before
	public void cleanUpData() {
		LOGGER.info("Instance of PublisherService is injected. Class is: {}",
				publisherService.getClass().getName());
		publisherService.deleteAll();
	}

	@Test
	public void basicCRUDTest() {
		Publisher publisher = createPublisher();
		publisherService.saveOrUpdate(publisher);

		Publisher publisherFromDb = publisherService.get(publisher.getId());
		Assert.assertNotNull(publisherFromDb);
		Assert.assertEquals(publisherFromDb.getName(), publisher.getName());

		publisherFromDb.setName("newName");
		publisherService.saveOrUpdate(publisherFromDb);
		Publisher publisherFromDbUpdated = publisherService.get(publisher
				.getId());
		Assert.assertEquals(publisherFromDb.getName(),
				publisherFromDbUpdated.getName());
		Assert.assertNotEquals(publisherFromDbUpdated.getName(),
				publisher.getName());

		publisherService.delete(publisherFromDbUpdated);
		Assert.assertNull(publisherService.get(publisher.getId()));
	}

	@Test
	public void uniqueConstraintsTest() {
		Publisher publisher = createPublisher();
		publisherService.saveOrUpdate(publisher);

		Publisher duplicatePublisher = createPublisher();
		duplicatePublisher.setName(publisher.getName());
		try {
			publisherService.saveOrUpdate(duplicatePublisher);
			Assert.fail("Not unique name can't be saved.");
		} catch (PersistenceException e) {
			// expected
		}

		// should be saved now
		duplicatePublisher.setName(randomString("name-"));
		publisherService.saveOrUpdate(duplicatePublisher);
	}

	@After
	public void finishTest() {
		publisherService.deleteAll();
		Assert.assertEquals(publisherService.getAll().size(), 0);
	}

}
