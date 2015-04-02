package by.stark.sample.dataaccess.impl;

import org.springframework.stereotype.Repository;

import by.stark.sample.dataaccess.UserDao;
import by.stark.sample.datamodel.User;

@Repository
public class UserDaoImpl extends AbstractDaoImpl<Long, User> implements UserDao {

	protected UserDaoImpl() {
		super(User.class);
	}
}
