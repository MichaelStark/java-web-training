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
import by.stark.sample.datamodel.Picture;

public class PictureServiceTest extends AbstractServiceTest {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PictureServiceTest.class);

	@Inject
	private PictureService pictureService;

	@Before
	public void cleanUpData() {
		LOGGER.info("Instance of PictureService is injected. Class is: {}",
				pictureService.getClass().getName());
		pictureService.deleteAll();
	}

	@Test
	public void basicCRUDTest() {
		Picture picture = createPicture();
		pictureService.saveOrUpdate(picture);

		Picture pictureFromDb = pictureService.get(picture.getId());
		Assert.assertNotNull(pictureFromDb);
		Assert.assertEquals(pictureFromDb.getName(), picture.getName());

		pictureFromDb.setName("newName");
		pictureService.saveOrUpdate(pictureFromDb);
		Picture pictureFromDbUpdated = pictureService.get(picture.getId());
		Assert.assertEquals(pictureFromDb.getName(),
				pictureFromDbUpdated.getName());
		Assert.assertNotEquals(pictureFromDbUpdated.getName(),
				picture.getName());

		pictureService.delete(pictureFromDbUpdated);
		Assert.assertNull(pictureService.get(picture.getId()));
	}

	@Test
	public void uniqueConstraintsTest() {
		Picture picture = createPicture();
		pictureService.saveOrUpdate(picture);

		Picture duplicatePicture = createPicture();
		duplicatePicture.setName(picture.getName());
		try {
			pictureService.saveOrUpdate(duplicatePicture);
			Assert.fail("Not unique name can't be saved.");
		} catch (PersistenceException e) {
			// expected
		}

		// should be saved now
		duplicatePicture.setName(randomString("name-"));
		pictureService.saveOrUpdate(duplicatePicture);
	}

	@After
	public void finishTest() {
		pictureService.deleteAll();
		Assert.assertEquals(pictureService.getAll().size(), 0);
	}

}
