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
import by.stark.sample.datamodel.Genre;

public class GenreServiceTest extends AbstractServiceTest {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(GenreServiceTest.class);

	@Inject
	private GenreService genreService;

	@Before
	public void cleanUpData() {
		LOGGER.info("Instance of GenreService is injected. Class is: {}",
				genreService.getClass().getName());
		genreService.deleteAll();
	}

	@Test
	public void basicCRUDTest() {
		Genre genre = createGenre();
		genreService.saveOrUpdate(genre);

		Genre genreFromDb = genreService.get(genre.getId());
		Assert.assertNotNull(genreFromDb);
		Assert.assertEquals(genreFromDb.getName(), genre.getName());

		genreFromDb.setName("newName");
		genreService.saveOrUpdate(genreFromDb);
		Genre genreFromDbUpdated = genreService.get(genre.getId());
		Assert.assertEquals(genreFromDb.getName(), genreFromDbUpdated.getName());
		Assert.assertNotEquals(genreFromDbUpdated.getName(), genre.getName());

		genreService.delete(genreFromDbUpdated);
		Assert.assertNull(genreService.get(genre.getId()));
	}

	@Test
	public void uniqueConstraintsTest() {
		Genre genre = createGenre();
		genreService.saveOrUpdate(genre);

		Genre duplicateGenre = createGenre();
		duplicateGenre.setName(genre.getName());
		try {
			genreService.saveOrUpdate(duplicateGenre);
			Assert.fail("Not unique name can't be saved.");
		} catch (PersistenceException e) {
			// expected
		}

		// should be saved now
		duplicateGenre.setName(randomString("name-"));
		genreService.saveOrUpdate(duplicateGenre);
	}

	@After
	public void finishTest() {
		genreService.deleteAll();
		Assert.assertEquals(genreService.getAll().size(), 0);
	}

}
