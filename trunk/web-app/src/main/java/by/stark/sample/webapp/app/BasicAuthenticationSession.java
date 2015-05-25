package by.stark.sample.webapp.app;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.request.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import by.stark.sample.datamodel.Userprofile;
import by.stark.sample.datamodel.enums.UserRole;
import by.stark.sample.services.UserService;

public class BasicAuthenticationSession extends AuthenticatedWebSession {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(BasicAuthenticationSession.class);

	public static final String ROLE_SIGNED_IN = "SIGNED_IN";
	private Userprofile user;

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
			this.user = user;
			authenticationResult = true;
		}
		return authenticationResult;
	}

	@Override
	public Roles getRoles() {
		if (isSignedIn() && (resultRoles == null)) {
			resultRoles = new Roles();
			List<UserRole> roles = userService.getRoles(user.getId());
			for (UserRole role : roles) {
				resultRoles.add(role.name());
			}
		}
		return resultRoles;
	}

	@Override
	public void signOut() {
		super.signOut();
		user = null;
	}

	public Userprofile getUser() {
		return user;
	}

	@Override
	public void setLocale(Locale locale) {
		super.setLocale(locale);
		System.out.println(locale == null ? "set locale to NULL" : locale
				.toString());

	}

	@Override
	public Locale getLocale() {
		return super.getLocale();
	}

}
