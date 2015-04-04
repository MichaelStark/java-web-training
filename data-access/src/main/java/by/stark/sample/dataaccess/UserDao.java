package by.stark.sample.dataaccess;

import java.util.List;

import by.stark.sample.datamodel.User;
import by.stark.sample.datamodel.enums.UserRole;
import by.stark.sample.datamodel.enums.UserStatus;

public interface UserDao extends AbstractDao<Long, User> {

	List<User> getAllUsersByRole(UserRole role);

	List<User> getAllUsersByStatus(UserStatus status);

}
