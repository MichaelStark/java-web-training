package by.stark.sample.webapp.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public abstract class BaseLayout extends WebPage {

	@Override
	protected void onInitialize() {
		super.onInitialize();
		add(new Label("headerTitle", getPageTitle()));
	}

	protected IModel<String> getPageTitle() {
		return new Model<String>(getClass().getSimpleName());
	}
}
