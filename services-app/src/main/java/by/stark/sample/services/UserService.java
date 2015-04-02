package by.stark.sample.services;

import org.springframework.transaction.annotation.Transactional;

import by.stark.sample.datamodel.User;

public interface UserService {

	@Transactional
	void createNewUser(User User);

	@Transactional
	User get(Long id);

	@Transactional
	void updateUser(User user);

	@Transactional
	void removeUser(Long id);
}
