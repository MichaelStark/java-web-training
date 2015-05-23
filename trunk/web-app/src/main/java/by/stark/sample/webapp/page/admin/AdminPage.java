package by.stark.sample.webapp.page.admin;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import by.stark.sample.webapp.page.BaseLayout;
import by.stark.sample.webapp.page.admin.edit.AuthorsEditPage;
import by.stark.sample.webapp.page.admin.edit.GenresEditPage;
import by.stark.sample.webapp.page.admin.edit.LibrarysEditPage;
import by.stark.sample.webapp.page.admin.edit.PublishersEditPage;
import by.stark.sample.webapp.page.admin.edit.UsersEditPage;

import com.googlecode.wicket.kendo.ui.widget.notification.Notification;

public class AdminPage extends BaseLayout {

	public final static Notification notification = new Notification(
			"notification");

	private List<String> menuItems = Arrays.asList(new ResourceModel(
			"p.admin.usersMenu").getObject(), new ResourceModel(
			"p.admin.authorsMenu").getObject(), new ResourceModel(
			"p.admin.genresMenu").getObject(), new ResourceModel(
			"p.admin.publishersMenu").getObject(), new ResourceModel(
			"p.admin.librarysMenu").getObject());

	String selectedMenu;

	public AdminPage() {
		super();
	};

	public AdminPage(String selectedMenu) {
		this.selectedMenu = selectedMenu;
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		add(new ListView<String>("admin-menu", menuItems) {
			@Override
			protected void populateItem(ListItem<String> item) {

				Link<Void> Link = new Link<Void>("linkToMenuItem") {

					@Override
					public void onClick() {
						if (item.getModelObject().equals(
								new ResourceModel("p.admin.usersMenu")
										.getObject())) {
							setResponsePage(new UsersEditPage());
						} else if (item.getModelObject().equals(
								new ResourceModel("p.admin.genresMenu")
										.getObject())) {
							setResponsePage(new GenresEditPage());
						} else if (item.getModelObject().equals(
								new ResourceModel("p.admin.authorsMenu")
										.getObject())) {
							setResponsePage(new AuthorsEditPage());
						} else if (item.getModelObject().equals(
								new ResourceModel("p.admin.publishersMenu")
										.getObject())) {
							setResponsePage(new PublishersEditPage());
						} else if (item.getModelObject().equals(
								new ResourceModel("p.admin.librarysMenu")
										.getObject())) {
							setResponsePage(new LibrarysEditPage());
						}
					}
				};

				item.add(Link);
				if (item.getModelObject().equals(selectedMenu)) {
					Link.add(new AttributeModifier("class", "selected_link"));
				}
				Link.add(new Label("menuItem", item.getModelObject()));

			}
		});
	}

	@Override
	protected IModel<String> getPageTitle() {
		return new ResourceModel("p.home.adminLink");
	}

}
