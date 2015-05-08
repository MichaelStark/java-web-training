package by.stark.sample.webapp.page.home.book;

import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import by.stark.sample.datamodel.Author;
import by.stark.sample.datamodel.Book;
import by.stark.sample.datamodel.Genre;
import by.stark.sample.services.BookService;
import by.stark.sample.webapp.page.home.HomePage;
import by.stark.sample.webapp.page.home.book.panel.BooksPanel;

public class BookPage extends HomePage {

	@Inject
	private BookService bookService;

	final List<Book> allBooks;

	public BookPage() {
		allBooks = bookService.getAll();
	}

	public BookPage(Author author) {
		allBooks = bookService.getAllByAuthor(author);
	}

	public BookPage(Genre genre) {
		allBooks = bookService.getAllByGenre(genre);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		add(new ListView<Book>("books-panel", allBooks) {
			@Override
			protected void populateItem(ListItem<Book> item) {
				Book book = item.getModelObject();
				item.add((new BooksPanel("book-details", book)));
			}
		});

		add(new Label("books-count", allBooks.size()));
	}

	@Override
	protected IModel<String> getPageTitle() {
		return new ResourceModel("p.home.title");
	}

}
