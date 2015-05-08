package by.stark.sample.webapp.page.home;

import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import by.stark.sample.datamodel.Genre;
import by.stark.sample.services.GenreService;
import by.stark.sample.webapp.page.BaseLayout;
import by.stark.sample.webapp.page.home.book.BookPage;

public class HomePage extends BaseLayout {

	@Inject
	private GenreService genreService;

	@Override
	protected void onInitialize() {
		super.onInitialize();

		final List<Genre> allGenres = genreService.getAll();

		add(new ListView<Genre>("genres-menu", allGenres) {
			@Override
			protected void populateItem(ListItem<Genre> item) {
				Genre genre = item.getModelObject();

				Link<Void> Link = new Link<Void>("linkToGenre") {

					@Override
					public void onClick() {
						setResponsePage(new BookPage(genre));
					}
				};

				item.add(Link);
				Link.add(new Label("genre", genre.getName()));

			}
		});
	}

	@Override
	protected IModel<String> getPageTitle() {
		return new ResourceModel("p.home.title");
	}

}
