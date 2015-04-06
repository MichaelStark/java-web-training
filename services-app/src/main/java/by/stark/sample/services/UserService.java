package by.stark.sample.services;

import java.util.List;

import by.stark.sample.datamodel.Userprofile;
import by.stark.sample.datamodel.enums.UserRole;
import by.stark.sample.datamodel.enums.UserStatus;

public interface UserService extends AbstractService<Long, Userprofile> {

	List<Userprofile> getAll();

	List<Userprofile> getAllUsersByRole(UserRole role);

	List<Userprofile> getAllUsersByStatus(UserStatus status);

}