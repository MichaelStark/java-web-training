package by.dzhivushko.sample.services;

import org.springframework.transaction.annotation.Transactional;

import by.dzhvisuhko.sample.datamodel.UserAccount;
import by.dzhvisuhko.sample.datamodel.UserProfile;

public interface UserService {

    @Transactional
    void createNewUser(UserProfile profile, UserAccount account);

    @Transactional
    UserProfile get(Long id);

    @Transactional
    void updateProfile(UserProfile profile);

    @Transactional
    void removeUser(Long id);
}
