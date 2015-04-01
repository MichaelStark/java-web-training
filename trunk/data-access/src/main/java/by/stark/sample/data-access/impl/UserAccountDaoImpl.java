package by.dzhivushko.sample.dataaccess.impl;

import org.springframework.stereotype.Repository;

import by.dzhivushko.sample.dataaccess.UserAccountDao;
import by.dzhvisuhko.sample.datamodel.UserAccount;

@Repository
public class UserAccountDaoImpl extends AbstractDaoImpl<Long, UserAccount> implements UserAccountDao {

    protected UserAccountDaoImpl() {
        super(UserAccount.class);
    }
}
