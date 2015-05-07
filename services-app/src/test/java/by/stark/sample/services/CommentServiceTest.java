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
import by.stark.sample.datamodel.Comment;

public class CommentServiceTest extends AbstractServiceTest {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CommentServiceTest.class);

	@Inject
	private BookService bookService;

	@Inject
	private PublisherService publisherService;

	@Inject
	private PictureService pictureService;

	@Inject
	private UserService userService;

	@Inject
	private CommentService commentService;

	@Before
	public void cleanUpData() {
		LOGGER.info("Instance of CommentService is injected. Class is: {}",
				commentService.getClass().getName());
		commentService.deleteAll();
	}

	@Test
	public void basicCRUDTest() {
		Comment comment = createCommentComplete();
		commentService.saveOrUpdate(comment);

		Comment commentFromDb = commentService.getById(comment.getId());
		Assert.assertNotNull(commentFromDb);
		Assert.assertEquals(commentFromDb.getRating(), comment.getRating());
		Assert.assertEquals(commentFromDb.getDescription(),
				comment.getDescription());
		Assert.assertEquals(commentFromDb.getBook(), comment.getBook());
		Assert.assertEquals(commentFromDb.getUser().getId(), comment.getUser()
				.getId());

		commentFromDb.setDescription("newDescription");
		commentService.saveOrUpdate(commentFromDb);
		Comment commentFromDbUpdated = commentService.get(comment.getId());
		Assert.assertEquals(commentFromDb.getDescription(),
				commentFromDbUpdated.getDescription());
		Assert.assertNotEquals(commentFromDbUpdated.getDescription(),
				comment.getDescription());

		commentService.delete(commentFromDbUpdated);
		Assert.assertNull(commentService.get(comment.getId()));
	}

	@Test
	public void searchByBookTest() {
		Comment comment = createCommentComplete();
		commentService.saveOrUpdate(comment);

		Comment anotherComment = createCommentComplete();
		commentService.saveOrUpdate(anotherComment);

		List<Comment> allComments = commentService.getAll();
		Assert.assertEquals(allComments.size(), 2);

		List<Comment> allCommentsByBook = commentService.getAllByBook(comment
				.getBook());
		Assert.assertEquals(allCommentsByBook.size(), 1);
		Assert.assertEquals(allCommentsByBook.get(0).getId(), comment.getId());

	}

	@Test
	public void uniqueConstraintsTest() {
		Comment comment = createCommentComplete();
		commentService.saveOrUpdate(comment);

		Comment duplicateComment = createCommentComplete();
		duplicateComment.setBook(comment.getBook());
		duplicateComment.setUser(comment.getUser());
		try {
			commentService.saveOrUpdate(duplicateComment);
			Assert.fail("Not unique book with user can't be saved.");
		} catch (PersistenceException e) {
			// expected
		}

		// should be saved now
		Comment tempComment = createCommentComplete();
		duplicateComment.setBook(tempComment.getBook());
		duplicateComment.setUser(tempComment.getUser());
		commentService.saveOrUpdate(duplicateComment);
	}

	@Test
	public void searchTest() {
		Comment comment = createCommentComplete();
		commentService.saveOrUpdate(comment);

		List<Comment> allComments = commentService.getAll();
		Assert.assertEquals(allComments.size(), 1);

	}

	@After
	public void finishTest() {
		commentService.deleteAll();
		Assert.assertEquals(commentService.getAll().size(), 0);
		bookService.deleteAll();
		Assert.assertEquals(bookService.getAll().size(), 0);
		userService.deleteAll();
		Assert.assertEquals(userService.getAll().size(), 0);
		publisherService.deleteAll();
		Assert.assertEquals(publisherService.getAll().size(), 0);
		pictureService.deleteAll();
		Assert.assertEquals(pictureService.getAll().size(), 0);
	}

	private Comment createCommentComplete() {
		Comment comment = createComment();
		pictureService.saveOrUpdate(comment.getBook().getPicture());
		publisherService.saveOrUpdate(comment.getBook().getPublisher());
		bookService.saveOrUpdate(comment.getBook());
		pictureService.saveOrUpdate(comment.getUser().getPicture());
		userService.saveOrUpdate(comment.getUser());
		return comment;
	}

}
