package by.stark.sample.webapp.page;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.http.handler.RedirectRequestHandler;

import by.stark.sample.datamodel.Userprofile;
import by.stark.sample.webapp.app.BasicAuthenticationSession;
import by.stark.sample.webapp.app.WicketWebApplication;
import by.stark.sample.webapp.page.admin.AdminPage;
import by.stark.sample.webapp.page.home.book.BookPage;
import by.stark.sample.webapp.page.librarian.Librarian4HandsPage;
import by.stark.sample.webapp.page.librarian.Librarian4RoomPage;
import by.stark.sample.webapp.page.login.LoginPage;
import by.stark.sample.webapp.page.user.User4RoomPage;

public abstract class BaseLayout extends WebPage {

	static final private int workStart = 9;
	static final private int workFinish = 21;

	public static int getWorkstart() {
		return workStart;
	}

	public static int getWorkfinish() {
		return workFinish;
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		Session session = getSession();

		add(new Link("linkToLogin") {
			@Override
			protected void onConfigure() {
				super.onConfigure();
				boolean isLogged = BasicAuthenticationSession.get()
						.isSignedIn();

				setVisible(!isLogged);
			}

			@Override
			public void onClick() {
				setResponsePage(new LoginPage());
			}
		});

		add(new Link("linkToLogout") {
			@Override
			protected void onConfigure() {
				super.onConfigure();
				setVisible(BasicAuthenticationSession.get().isSignedIn());
			}

			@Override
			public void onClick() {
				final HttpServletRequest servletReq = (HttpServletRequest) getRequest()
						.getContainerRequest();
				servletReq.getSession().invalidate();
				getSession().invalidate();
				getRequestCycle().scheduleRequestHandlerAfterCurrent(
						new RedirectRequestHandler(
								WicketWebApplication.LOGIN_URL));

			}
		});
		Userprofile user = BasicAuthenticationSession.get().getUser();
		Link userProfileLink = new Link("userProfile") {

			@Override
			public void onClick() {
				if (user != null) {
					setResponsePage(new User4RoomPage());
				}
			}

		};
		add(userProfileLink);
		userProfileLink.add(new Label("userName", new Model(user != null ? user
				.getEmail() : null)));

		add(new Link("ru") {

			@Override
			public void onClick() {
				getSession().setLocale(new Locale("ru", "RU"));
				setResponsePage(new BookPage());
			}
		});

		add(new Link("en") {

			@Override
			public void onClick() {
				getSession().setLocale(new Locale("en", "US"));
				setResponsePage(new BookPage());
			}
		});

		add(new Label("headerTitle", getPageTitle()));

		add(new SecuredLinkForAdmin("linkToAdmin") {

			@Override
			public void onClick() {
				setResponsePage(new AdminPage());
			}
		});
		add(new SecuredLinkForLibrarian("linkToRoomWork") {

			@Override
			public void onClick() {
				setResponsePage(new Librarian4RoomPage());
			}
		});
		add(new SecuredLinkForLibrarian("linkToHandsWork") {

			@Override
			public void onClick() {
				setResponsePage(new Librarian4HandsPage());
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

	}

	@AuthorizeAction(action = Action.RENDER, roles = { "librarian" })
	protected abstract class SecuredLinkForLibrarian extends Link<Void> {

		public SecuredLinkForLibrarian(String id) {
			super(id);
		}
	}

	@AuthorizeAction(action = Action.RENDER, roles = { "admin" })
	protected abstract class SecuredLinkForAdmin extends Link<Void> {

		public SecuredLinkForAdmin(String id) {
			super(id);
		}
	}

	@AuthorizeAction(action = Action.RENDER, roles = { "admin" })
	protected abstract class SecuredAjaxLinkForAdmin extends AjaxLink<Void> {

		public SecuredAjaxLinkForAdmin(String id) {
			super(id);
		}
	}

	protected IModel<String> getPageTitle() {
		return new Model<String>(getClass().getSimpleName());
	}
}
