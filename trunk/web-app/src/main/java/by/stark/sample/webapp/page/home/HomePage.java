package by.stark.sample.webapp.page.home;

import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import by.stark.sample.datamodel.Book;
import by.stark.sample.datamodel.Genre;
import by.stark.sample.services.BookService;
import by.stark.sample.services.GenreService;
import by.stark.sample.webapp.page.BaseLayout;
import by.stark.sample.webapp.page.home.panel.BooksPanel;

public class HomePage extends BaseLayout {

	@Inject
	private GenreService genreService;
	@Inject
	private BookService bookService;

	@Override
	protected void onInitialize() {
		super.onInitialize();

		final List<Book> allBooks = bookService.getAll();

		add(new ListView<Book>("books-panel", allBooks) {
			@Override
			protected void populateItem(ListItem<Book> item) {
				Book book = item.getModelObject();
				item.add((new BooksPanel("book-details", book)));
			}
		});

		final List<Genre> allGenres = genreService.getAll();

		add(new ListView<Genre>("genres-menu", allGenres) {
			@Override
			protected void populateItem(ListItem<Genre> item) {
				Genre genre = item.getModelObject();
				item.add(new Label("genre", genre.getName()));

			}
		});

		add(new Label("books-count", allBooks.size()));

	}

	@Override
	protected IModel<String> getPageTitle() {
		return new ResourceModel("p.home.title");
	}

}
