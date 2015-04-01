package by.dzhivushko.sample.dataaccess.impl;

import org.springframework.stereotype.Repository;

import by.dzhivushko.sample.dataaccess.UserProfileDao;
import by.dzhvisuhko.sample.datamodel.UserProfile;

@Repository
public class UserProfileDaoImpl extends AbstractDaoImpl<Long, UserProfile> implements UserProfileDao {

    protected UserProfileDaoImpl() {
        super(UserProfile.class);
    }
}
