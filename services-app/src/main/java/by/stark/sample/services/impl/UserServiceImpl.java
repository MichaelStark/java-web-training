package by.stark.sample.services.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import by.stark.sample.dataaccess.UserDao;
import by.stark.sample.datamodel.User;
import by.stark.sample.datamodel.enums.UserRole;
import by.stark.sample.datamodel.enums.UserStatus;
import by.stark.sample.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserServiceImpl.class);

	@Inject
	private UserDao dao;

	@PostConstruct
	private void init() {
		// this method will be called by Spring after bean instantiation. Can be
		// used for any initialization process.
		LOGGER.info("Instance of UserService is created. Class is: {}",
				getClass().getName());
	}

	@Override
	public User get(Long id) {
		User entity = dao.getById(id);
		return entity;
	}

	@Override
	public void saveOrUpdate(User user) {
		if (user.getId() == null) {
			LOGGER.debug("Save new: {}", user);
			dao.insert(user);
		} else {
			LOGGER.debug("Update: {}", user);
			dao.update(user);
		}
	}

	@Override
	public void delete(User user) {
		LOGGER.debug("Remove: {}", user);
		dao.delete(user.getId());
	}

	@Override
	public void deleteAll() {
		LOGGER.debug("Remove all users");
		dao.deleteAll();
	}

	@Override
	public List<User> getAllUsersByRole(UserRole role) {
		return dao.getAllUsersByRole(role);
	}

	@Override
	public List<User> getAllUsersByStatus(UserStatus status) {
		return dao.getAllUsersByStatus(status);
	}

}
