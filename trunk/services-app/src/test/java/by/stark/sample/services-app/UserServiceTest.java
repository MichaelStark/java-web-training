package by.dzhivushko.sample.services;

import java.util.Arrays;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import org.junit.Assert;
import org.junit.Test;

import by.dzhivushko.sample.AbstractServiceTest;
import by.dzhvisuhko.sample.datamodel.UserAccount;
import by.dzhvisuhko.sample.datamodel.UserProfile;
import by.dzhvisuhko.sample.datamodel.enums.UserStatus;

public class UserServiceTest extends AbstractServiceTest {

    @Inject
    private UserService userService;

    @Test
    public void crudTest() {

        final UserProfile profile = createUserProfile();
        final UserAccount account = createUserAccount();
        userService.createNewUser(profile, account);

        final UserProfile createdUser = userService.get(profile.getId());
        Assert.assertNotNull(createdUser);
        // TODO check equals

        userService.removeUser(profile.getId());
        Assert.assertNull(userService.get(profile.getId()));
    }

    @Test
    public void uniqueConstraintsTest() {
        final UserProfile profile = createUserProfile();
        final String email = randomString("email");
        final UserAccount account = createUserAccount();
        account.setEmail(email);
        profile.setUserAccount(account);
        userService.createNewUser(profile, account);

        final UserProfile duplicateProfile = createUserProfile();
        final UserAccount duplicateAccount = createUserAccount();
        duplicateAccount.setEmail(email);
        duplicateProfile.setUserAccount(duplicateAccount);
        try {
            userService.createNewUser(duplicateProfile, duplicateAccount);
            Assert.fail("Not unique login can't be saved.");
        } catch (final PersistenceException e) {
            // expected
        }

        // should be saved now
        duplicateAccount.setEmail(randomString("email"));
        userService.createNewUser(duplicateProfile, duplicateAccount);
    }

    private UserAccount createUserAccount() {
        final UserAccount userAccount = new UserAccount();
        userAccount.setEmail(randomString("email-"));
        userAccount.setPassword(randomString("password"));
        return userAccount;
    }

    private UserProfile createUserProfile() {
        final UserProfile userProfile = new UserProfile();
        userProfile.setFirstName(randomString("firstName-"));
        userProfile.setLastName(randomString("lastName-"));
        userProfile.setMiddleName(randomString("middleName-"));
        userProfile.setStatus(randomFromCollection(Arrays.asList(UserStatus.values())));
        userProfile.setCreated(randomDate());
        return userProfile;
    }
}
