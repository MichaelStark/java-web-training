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
				.getAllByName(firstName);
		Assert.assertEquals(allAuthorsByFirstName.size(), 1);
		Assert.assertEquals(allAuthorsByFirstName.get(0).getId(),
				author.getId());

	}

	@Test
	public void searchingByName() {
		Author author1 = new Author();
		author1.setFirstName("1");
		author1.setLastName("4");
		authorService.saveOrUpdate(author1);

		Author author2 = new Author();
		author2.setFirstName("1");
		author2.setLastName("5");
		authorService.saveOrUpdate(author2);

		Author author3 = new Author();
		author3.setFirstName("2");
		author3.setLastName("4");
		authorService.saveOrUpdate(author3);

		Author author4 = new Author();
		author4.setFirstName("4");
		author4.setLastName("1");
		authorService.saveOrUpdate(author4);

		List<Author> authors = authorService.getAllByName("1");
		Assert.assertEquals(authors.size(), 3);

		authors = authorService.getAllByName("1", "4");
		Assert.assertEquals(authors.size(), 2);
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
