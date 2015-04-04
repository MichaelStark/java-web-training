package by.stark.sample.services;

import java.util.List;

import by.stark.sample.datamodel.User;
import by.stark.sample.datamodel.enums.UserRole;
import by.stark.sample.datamodel.enums.UserStatus;

public interface UserService extends AbstractService<Long, User> {

	List<User> getAllUsersByRole(UserRole role);

	List<User> getAllUsersByStatus(UserStatus status);

}