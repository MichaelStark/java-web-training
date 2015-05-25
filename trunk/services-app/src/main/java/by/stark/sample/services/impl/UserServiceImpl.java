package by.stark.sample.services.impl;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import by.stark.sample.dataaccess.UserDao;
import by.stark.sample.datamodel.Userprofile;
import by.stark.sample.datamodel.Userprofile_;
import by.stark.sample.datamodel.enums.UserRole;
import by.stark.sample.datamodel.enums.UserStatus;
import by.stark.sample.services.UserService;

@Service
public class UserServiceImpl extends AbstractServiceImpl<Long, Userprofile>
		implements UserService {

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
	public Userprofile getByEmail(String email) {
		final List<Userprofile> allByFieldRestriction = dao
				.getAllByFieldRestriction(Userprofile_.email, email);
		return !allByFieldRestriction.isEmpty() ? allByFieldRestriction.get(0)
				: null;
	}

	@Override
	public List<Userprofile> getAllByRole(UserRole role) {
		return dao.getAllByFieldRestriction(Userprofile_.role, role,
				Userprofile_.picture);
	}

	@Override
	public List<Userprofile> getAllByStatus(UserStatus status) {
		return dao.getAllByFieldRestriction(Userprofile_.status, status,
				Userprofile_.picture);
	}

	@Override
	public List<UserRole> getRoles(Long userId) {
		return Arrays.asList(dao.getById(userId).getRole());
	}

	@Override
	public Userprofile getById(Long id) {
		return dao.getById(Userprofile_.id, id, Userprofile_.picture);
	}

}
