package by.stark.sample.webapp.app;

import javax.inject.Inject;

import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.request.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import by.stark.sample.datamodel.Userprofile;
import by.stark.sample.services.UserService;

public class BasicAuthenticationSession extends AuthenticatedWebSession {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(BasicAuthenticationSession.class);

	public static final String ROLE_SIGNED_IN = "SIGNED_IN";
	private Long userId;

	private Roles resultRoles;

	@Inject
	private UserService userService;

	public BasicAuthenticationSession(final Request request) {
		super(request);
		Injector.get().inject(this);
	}

	public static BasicAuthenticationSession get() {
		return (BasicAuthenticationSession) Session.get();
	}

	@Override
	public boolean authenticate(final String userName, final String password) {
		boolean authenticationResult = false;
		final Userprofile user = userService.getByEmail(userName);
		if (user != null && user.getPassword().equals(password)) {
			this.userId = user.getId();
			authenticationResult = true;
		}
		return authenticationResult;
	}

	@Override
	public Roles getRoles() {
		if (isSignedIn() && (resultRoles == null)) {
			resultRoles = new Roles();
			resultRoles.add(ROLE_SIGNED_IN);
			resultRoles.addAll(userService.getRoles(userId));
		}
		return resultRoles;
	}

	@Override
	public void signOut() {
		super.signOut();
		userId = null;
	}

}
