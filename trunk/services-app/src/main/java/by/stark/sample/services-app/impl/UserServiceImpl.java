package by.dzhivushko.sample.services.impl;

import javax.inject.Inject;

import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;

import by.dzhivushko.sample.dataaccess.UserAccountDao;
import by.dzhivushko.sample.dataaccess.UserProfileDao;
import by.dzhivushko.sample.services.UserService;
import by.dzhvisuhko.sample.datamodel.UserAccount;
import by.dzhvisuhko.sample.datamodel.UserProfile;

@Service
public class UserServiceImpl implements UserService {

    @Inject
    private UserAccountDao accountDao;
    @Inject
    private UserProfileDao profileDao;

    @Override
    public void createNewUser(UserProfile profile, UserAccount account) {
    /*    Validate.isTrue(account.getId() == null, "This method should be called for 'not saved yet' account only. Use UPDATE instead");
        accountDao.insert(account);*/

        Validate.isTrue(profile.getId() == null, "This method should be called for 'not saved yet' profile only. Use UPDATE instead");
        profile.setUserAccount(account);
        profileDao.insert(profile);
    }

    @Override
    public UserProfile get(Long id) {
        return profileDao.getById(id);
    }

    @Override
    public void updateProfile(UserProfile profile) {
        profileDao.update(profile);
    }

    @Override
    public void removeUser(Long id) {

        profileDao.delete(id);
        accountDao.delete(id);

    }
}
