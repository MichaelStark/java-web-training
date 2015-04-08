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
import by.stark.sample.datamodel.Picture;
import by.stark.sample.datamodel.Userprofile;
import by.stark.sample.datamodel.Userprofile_;
import by.stark.sample.datamodel.enums.UserRole;
import by.stark.sample.datamodel.enums.UserStatus;

public class UserServiceTest extends AbstractServiceTest {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserServiceTest.class);

	@Inject
	private UserService userService;

	@Inject
	private PictureService pictureService;

	@Before
	public void cleanUpData() {
		LOGGER.info("Instance of UserService is injected. Class is: {}",
				userService.getClass().getName());
		userService.deleteAll();
	}

	@Test
	public void basicCRUDTest() {
		Userprofile userprofile = createUserComplete();
		userService.saveOrUpdate(userprofile);

		Userprofile userFromDb = userService.get(Userprofile_.id,
				userprofile.getId(), Userprofile_.picture);
		Assert.assertNotNull(userFromDb);
		Assert.assertEquals(userFromDb.getRole(), userprofile.getRole());
		Assert.assertEquals(userFromDb.getEmail(), userprofile.getEmail());
		Assert.assertEquals(userFromDb.getFirstName(),
				userprofile.getFirstName());
		Assert.assertEquals(userFromDb.getLastName(), userprofile.getLastName());
		Assert.assertEquals(userFromDb.getPassword(), userprofile.getPassword());
		Assert.assertEquals(userFromDb.getGender(), userprofile.getGender());
		Assert.assertEquals(userFromDb.getStatus(), userprofile.getStatus());
		Assert.assertTrue(userFromDb.getBirthday().compareTo(
				userprofile.getBirthday()) == 0);
		Assert.assertEquals(userFromDb.getPicture().getId(), userprofile
				.getPicture().getId());

		userFromDb.setFirstName("newFirstName");
		userService.saveOrUpdate(userFromDb);
		Userprofile userFromDbUpdated = userService.get(userprofile.getId());
		Assert.assertEquals(userFromDb.getFirstName(),
				userFromDbUpdated.getFirstName());
		Assert.assertNotEquals(userFromDbUpdated.getFirstName(),
				userprofile.getFirstName());

		userService.delete(userFromDbUpdated);
		Assert.assertNull(userService.get(userprofile.getId()));
	}

	@Test
	public void searchByRoleTest() {
		Userprofile userprofile = createUserComplete();
		UserRole role = userprofile.getRole();
		userService.saveOrUpdate(userprofile);

		Userprofile anotherUser = createUserComplete();
		UserRole anotherRole = anotherUser.getRole();
		userService.saveOrUpdate(anotherUser);

		List<Userprofile> allUsers = userService.getAll();
		Assert.assertEquals(allUsers.size(), 2);

		List<Userprofile> allUsersByRole = userService.getAllByField(
				Userprofile_.role, role);
		if (anotherRole != role) {
			Assert.assertEquals(allUsersByRole.size(), 1);
			Assert.assertEquals(allUsersByRole.get(0).getId(),
					userprofile.getId());
		} else {
			Assert.assertEquals(allUsersByRole.size(), 2);
		}

	}

	@Test
	public void searchByStatusTest() {
		Userprofile userprofile = createUserComplete();
		UserStatus status = userprofile.getStatus();
		userService.saveOrUpdate(userprofile);

		Userprofile anotherUser = createUserComplete();
		UserStatus anotherStatus = anotherUser.getStatus();
		userService.saveOrUpdate(anotherUser);

		List<Userprofile> allUsers = userService.getAll();
		Assert.assertEquals(allUsers.size(), 2);

		List<Userprofile> allUsersByStatus = userService.getAllByField(
				Userprofile_.status, status);
		if (anotherStatus != status) {
			Assert.assertEquals(allUsersByStatus.size(), 1);
			Assert.assertEquals(allUsersByStatus.get(0).getId(),
					userprofile.getId());
		} else {
			Assert.assertEquals(allUsersByStatus.size(), 2);
		}

	}

	@Test
	public void uniqueConstraintsTest() {
		Userprofile userprofile = createUserComplete();
		userService.saveOrUpdate(userprofile);
		Picture picture = createPicture();
		pictureService.saveOrUpdate(picture);

		Userprofile duplicateUser = createUserComplete();

		duplicateUser.setEmail(userprofile.getEmail());
		try {
			userService.saveOrUpdate(duplicateUser);
			Assert.fail("Not unique email can't be saved.");
		} catch (PersistenceException e) {
			// expected
		}

		duplicateUser.setPicture(userprofile.getPicture());
		try {
			userService.saveOrUpdate(duplicateUser);
			Assert.fail("Not unique picture can't be saved.");
		} catch (PersistenceException e) {
			// expected
		}

		// should be saved now
		duplicateUser.setEmail(randomString("email-"));
		duplicateUser.setPicture(picture);
		userService.saveOrUpdate(duplicateUser);
	}

	@Test
	public void searchTest() {
		Userprofile userprofile = createUserComplete();
		userService.saveOrUpdate(userprofile);

		List<Userprofile> allUsers = userService.getAll();
		Assert.assertEquals(allUsers.size(), 1);

	}

	@After
	public void finishTest() {
		userService.deleteAll();
		Assert.assertEquals(userService.getAll().size(), 0);
		pictureService.deleteAll();
		Assert.assertEquals(pictureService.getAll().size(), 0);
	}

	private Userprofile createUserComplete() {
		Userprofile userprofile = createUser();
		pictureService.saveOrUpdate(userprofile.getPicture());
		return userprofile;
	}

}
