package by.stark.sample.services;

import java.util.List;

import by.stark.sample.datamodel.Userprofile;
import by.stark.sample.datamodel.enums.UserRole;
import by.stark.sample.datamodel.enums.UserStatus;

public interface UserService extends AbstractService<Long, Userprofile> {

	Userprofile getById(Long id);

	Userprofile getByEmail(String email);

	List<Userprofile> getAllByRole(UserRole role);

	List<Userprofile> getAllByStatus(UserStatus status);

	List<String> getRoles(Long userId);

}