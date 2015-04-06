package by.stark.sample.dataaccess;

import java.util.List;

import by.stark.sample.datamodel.Userprofile;
import by.stark.sample.datamodel.enums.UserRole;
import by.stark.sample.datamodel.enums.UserStatus;

public interface UserDao extends AbstractDao<Long, Userprofile> {

	List<Userprofile> getAllUsersByRole(UserRole role);

	List<Userprofile> getAllUsersByStatus(UserStatus status);

}
