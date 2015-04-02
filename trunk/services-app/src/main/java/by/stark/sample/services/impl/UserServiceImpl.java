package by.stark.sample.services.impl;

import javax.inject.Inject;

import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;

import by.stark.sample.dataaccess.UserDao;
import by.stark.sample.datamodel.User;
import by.stark.sample.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Inject
	private UserDao userDao;

	@Override
	public void createNewUser(User user) {
		Validate.isTrue(
				user.getId() == null,
				"This method should be called for 'not saved yet' user only. Use UPDATE instead");
		userDao.insert(user);
	}

	@Override
	public User get(Long id) {
		return userDao.getById(id);
	}

	@Override
	public void updateUser(User user) {
		userDao.update(user);
	}

	@Override
	public void removeUser(Long id) {
		userDao.delete(id);
	}

}
