package by.stark.sample.webapp.page;

import java.util.Locale;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import by.stark.sample.webapp.page.admin.AdminPage;
import by.stark.sample.webapp.page.home.book.BookPage;

public abstract class BaseLayout extends WebPage {

	@Override
	protected void onInitialize() {
		super.onInitialize();

		Session session = getSession();
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

		add(new Link<Void>("linkToAdmin") {

			@Override
			public void onClick() {
				setResponsePage(new AdminPage());
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

	protected IModel<String> getPageTitle() {
		return new Model<String>(getClass().getSimpleName());
	}
}
