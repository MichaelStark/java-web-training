package by.stark.sample.webapp.page.login;

import java.util.Locale;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.ResourceModel;

import by.stark.sample.webapp.page.home.book.BookPage;

public class LoginPage extends WebPage {

	@Override
	protected void onInitialize() {
		super.onInitialize();

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

		add(new AjaxLink("linkToRegistration") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				setResponsePage(new BookPage());
			}
		});

		Form form = new Form("form");
		add(form);

		form.add(new EmailTextField("signinEmail").add(new AttributeModifier(
				"placeholder", new ResourceModel("p.admin.users.email"))));

		form.add(new PasswordTextField("signinPassword")
				.add(new AttributeModifier("placeholder", new ResourceModel(
						"p.admin.users.password"))));

		AjaxButton signin = new AjaxButton("signinSubmit") {
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> unused) {
			}
		};
		form.add(signin);
		signin.add(new AttributeModifier("value", new ResourceModel(
				"p.home.loginLink")));
	}
}
