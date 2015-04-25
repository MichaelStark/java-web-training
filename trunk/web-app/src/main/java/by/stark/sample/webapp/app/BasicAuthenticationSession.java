package by.stark.sample.webapp.app;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.inject.Inject;

import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.request.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import by.stark.sample.webapp.app.auth.AuthenticationManager;

public class BasicAuthenticationSession extends AuthenticatedWebSession {

	private static final Logger LOGGER = LoggerFactory.getLogger(BasicAuthenticationSession.class);

	public static final String ROLE_SIGNED_IN = "SIGNED_IN";
	private String userName;

	private Roles resultRoles;

	@Inject
	private AuthenticationManager authenticationManager;

	public BasicAuthenticationSession(final Request request) {
		super(request);
		Injector.get().inject(this);
	}

	public static BasicAuthenticationSession get() {
		return (BasicAuthenticationSession) Session.get();
	}

	@Override
	public boolean authenticate(final String userName, final String password) {
		boolean authenticationResult;
		try {
			// TODO use user service
			authenticationResult = authenticationManager.authenticate(userName, password);
			this.userName = userName;
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			LOGGER.error("Internal error on attempt to authenticate user.", e);
			return false;
		}
		return authenticationResult;
	}

	@Override
	public Roles getRoles() {
		if (isSignedIn() && (resultRoles == null)) {
			resultRoles = new Roles();
			resultRoles.add(ROLE_SIGNED_IN);
			// TODO use user service
			resultRoles.addAll(authenticationManager.resolveRoles(userName));
		}
		return resultRoles;
	}

	@Override
	public void signOut() {
		super.signOut();
		userName = null;
	}

	public String getUserName() {
		return userName;
	}

}
