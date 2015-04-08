package by.stark.sample.dataaccess.impl;

import org.springframework.stereotype.Repository;

import by.stark.sample.dataaccess.UserDao;
import by.stark.sample.datamodel.Userprofile;

@Repository
public class UserDaoImpl extends AbstractDaoImpl<Long, Userprofile> implements
		UserDao {

	protected UserDaoImpl() {
		super(Userprofile.class);
	}

}
