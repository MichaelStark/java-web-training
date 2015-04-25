package by.stark.sample.webapp.app.auth;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

public interface AuthenticationManager {

    boolean authenticate(String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException;

    List<String> resolveRoles(String username);

}
