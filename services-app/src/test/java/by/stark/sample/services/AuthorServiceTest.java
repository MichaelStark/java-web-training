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
import by.stark.sample.datamodel.Author;

public class AuthorServiceTest extends AbstractServiceTest {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AuthorServiceTest.class);

	@Inject
	private AuthorService authorService;

	@Before
	public void cleanUpData() {
		LOGGER.info("Instance of AuthorService is injected. Class is: {}",
				authorService.getClass().getName());
		authorService.deleteAll();
	}

	@Test
	public void basicCRUDTest() {
		Author author = createAuthor();
		authorService.saveOrUpdate(author);

		Author authorFromDb = authorService.get(author.getId());
		Assert.assertNotNull(authorFromDb);
		Assert.assertEquals(authorFromDb.getFirstName(), author.getFirstName());
		Assert.assertEquals(authorFromDb.getLastName(), author.getLastName());

		authorFromDb.setFirstName("newFirstName");
		authorService.saveOrUpdate(authorFromDb);
		Author authorFromDbUpdated = authorService.get(author.getId());
		Assert.assertEquals(authorFromDb.getFirstName(),
				authorFromDbUpdated.getFirstName());
		Assert.assertNotEquals(authorFromDbUpdated.getFirstName(),
				author.getFirstName());

		authorService.delete(authorFromDbUpdated);
		Assert.assertNull(authorService.get(author.getId()));
	}

	@Test
	public void searchByFirstNameTest() {
		Author author = createAuthor();
		String firstName = author.getFirstName();
		authorService.saveOrUpdate(author);

		Author anotherAuthor = createAuthor();
		authorService.saveOrUpdate(anotherAuthor);

		List<Author> allAuthors = authorService.getAll();
		Assert.assertEquals(allAuthors.size(), 2);

		List<Author> allAuthorsByFirstName = authorService
				.getAllAuthorsByFirstName(firstName);
		Assert.assertEquals(allAuthorsByFirstName.size(), 1);
		Assert.assertEquals(allAuthorsByFirstName.get(0).getId(),
				author.getId());

	}

	@Test
	public void searchByLastNameTest() {
		Author author = createAuthor();
		String lastName = author.getLastName();
		authorService.saveOrUpdate(author);

		Author anotherAuthor = createAuthor();
		authorService.saveOrUpdate(anotherAuthor);

		List<Author> allAuthors = authorService.getAll();
		Assert.assertEquals(allAuthors.size(), 2);

		List<Author> allAuthorsByLastName = authorService
				.getAllAuthorsByLastName(lastName);
		Assert.assertEquals(allAuthorsByLastName.size(), 1);
		Assert.assertEquals(allAuthorsByLastName.get(0).getId(), author.getId());

	}

	@Test
	public void uniqueConstraintsTest() {
		Author author = createAuthor();
		authorService.saveOrUpdate(author);

		Author duplicateAuthor = createAuthor();
		duplicateAuthor.setFirstName(author.getFirstName());
		duplicateAuthor.setLastName(author.getLastName());
		try {
			authorService.saveOrUpdate(duplicateAuthor);
			Assert.fail("Not unique first with last names can't be saved.");
		} catch (PersistenceException e) {
			// expected
		}

		// should be saved now
		duplicateAuthor.setFirstName(randomString("firstName-"));
		duplicateAuthor.setLastName(randomString("lastName-"));
		authorService.saveOrUpdate(duplicateAuthor);
	}

	@Test
	public void searchTest() {
		Author author = createAuthor();
		authorService.saveOrUpdate(author);

		List<Author> allAuthors = authorService.getAll();
		Assert.assertEquals(allAuthors.size(), 1);

	}

	@After
	public void finishTest() {
		authorService.deleteAll();
		Assert.assertEquals(authorService.getAll().size(), 0);
	}

}
