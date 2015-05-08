package by.stark.sample.webapp.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import by.stark.sample.webapp.page.home.book.BookPage;

public abstract class BaseLayout extends WebPage {

	@Override
	protected void onInitialize() {
		super.onInitialize();

		add(new Label("headerTitle", getPageTitle()));
		add(new Link<Void>("linkToHome") {

			@Override
			public void onClick() {
				setResponsePage(new BookPage());
			}
		});
	}

	protected IModel<String> getPageTitle() {
		return new Model<String>(getClass().getSimpleName());
	}
}
