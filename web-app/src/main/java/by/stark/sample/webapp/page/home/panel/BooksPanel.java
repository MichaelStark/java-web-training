package by.stark.sample.webapp.page.home.panel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import by.stark.sample.datamodel.Author;
import by.stark.sample.datamodel.Book;
import by.stark.sample.services.BookService;

public class BooksPanel extends Panel {
	@Inject
	private BookService bookService;
	private Book book;

	public BooksPanel(String id, Book book) {
		super(id);
		this.book = book;
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		List<Author> allAuthors = new ArrayList<Author>();
		allAuthors.addAll(book.getAuthors());

		// add(new Label("author", new
		// Model<String>(allAuthors.get(0).getFirstName() + " " +
		// allAuthors.get(0).getLastName())));

		add(new ListView<Author>("authors-list", allAuthors) {

			@Override
			protected void populateItem(ListItem<Author> item) {
				Author author = item.getModelObject();
				item.add(new Label("author", new Model<String>(author
						.getFirstName().substring(0, 1)
						+ ". "
						+ author.getLastName())));

			}
		});

		add(new Label("title", new Model<String>(book.getTitle())));
		add(new Label("pages", new Model<Long>(book.getPages())));
		add(new Label("publisher", new Model<String>(book.getPublisher()
				.getName())));
		add(new Label("year", new Model<Long>(book.getYear())));
		add(new Label("isbn", new Model<String>(book.getIsbn())));

	}
}
