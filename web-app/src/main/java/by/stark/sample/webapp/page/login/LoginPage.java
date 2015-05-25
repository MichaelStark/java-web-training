package by.stark.sample.webapp.page.login;

import java.util.Locale;

import javax.inject.Inject;

import org.apache.wicket.Application;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.string.Strings;

import by.stark.sample.datamodel.Userprofile;
import by.stark.sample.datamodel.enums.UserRole;
import by.stark.sample.services.UserService;
import by.stark.sample.webapp.page.home.book.BookPage;

import com.googlecode.wicket.jquery.ui.panel.JQueryFeedbackPanel;

public class LoginPage extends WebPage {

	@Inject
	private UserService userService;

	public static final String ID_FORM = "form";
	public static final String ID_PASSWORD = "password";
	public static final String ID_USERNAME = "email";

	private String email;
	private String password;

	@Override
	protected void onInitialize() {
		super.onInitialize();

		if (AuthenticatedWebSession.get().isSignedIn()) {
			setResponsePage(Application.get().getHomePage());
		}

		Session session = getSession();

		add(new Link("ru") {

			@Override
			public void onClick() {
				getSession().setLocale(new Locale("ru", "RU"));
				setResponsePage(new LoginPage());
			}
		});

		add(new Link("en") {

			@Override
			public void onClick() {
				getSession().setLocale(new Locale("en", "US"));
				setResponsePage(new LoginPage());
			}
		});

		Link<Void> logo = new Link<Void>("linkToHome") {

			@Override
			public void onClick() {
				setResponsePage(new BookPage());
			}
		};
		add(logo);
		Image logoI = new Image("logo", "");
		logo.add(logoI);
		if (getSession().getLocale().equals(new Locale("en", "US"))) {
			logoI.add(new AttributeModifier("src", "../images/en/logo.png"));
		} else if (getSession().getLocale().equals(new Locale("ru", "RU"))) {
			logoI.add(new AttributeModifier("src", "../images/ru/logo.png"));
		} else {
			logoI.add(new AttributeModifier("src", "../images/logo.png"));
		}

		Form<Userprofile> form = new Form<Userprofile>(ID_FORM);
		form.setDefaultModel(new CompoundPropertyModel<LoginPage>(this));

		form.add(new EmailTextField(ID_USERNAME).setRequired(true).add(
				new AttributeModifier("placeholder", new ResourceModel(
						"p.admin.users.email"))));

		form.add(new PasswordTextField(ID_PASSWORD).setRequired(true).add(
				new AttributeModifier("placeholder", new ResourceModel(
						"p.admin.users.password"))));

		final FeedbackPanel feedback = new JQueryFeedbackPanel("feedback");
		form.add(feedback.setOutputMarkupId(true));

		AjaxButton signin = new AjaxButton("signinSubmit") {
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.add(feedback);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> unused) {
				if (Strings.isEmpty(email) || Strings.isEmpty(password)) {
					return;
				}
				final boolean authResult = AuthenticatedWebSession.get()
						.signIn(email, password);
				if (authResult) {
					// continueToOriginalDestination();
					setResponsePage(Application.get().getHomePage());
				} else {
					feedback.error(new ResourceModel(
							"p.login.authorizationError").getObject());
					target.add(feedback);
				}
			}
		};

		form.add(new AjaxButton("linkToRegistration") {
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.add(feedback);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				if (Strings.isEmpty(email) || Strings.isEmpty(password)) {
					return;
				}
				final boolean alreadyEx = userService.getByEmail(email) == null ? false
						: true;
				if (alreadyEx) {
					feedback.error(new ResourceModel(
							"p.login.registrationError").getObject());
					target.add(feedback);
				} else if (password.length() < 21) {
					Userprofile user = new Userprofile();
					user.setEmail(email);
					user.setPassword(password);
					user.setRole(UserRole.reader);
					userService.saveOrUpdate(user);
					AuthenticatedWebSession.get().signIn(email, password);
					setResponsePage(Application.get().getHomePage());
				} else if (password.length() > 20) {
					feedback.error(new ResourceModel(
							"p.login.passwordLimitError").getObject());

					target.add(feedback);
				}
			}
		});

		form.add(signin);
		signin.add(new AttributeModifier("value", new ResourceModel(
				"p.home.loginLink")));

		add(form);
	}
}
