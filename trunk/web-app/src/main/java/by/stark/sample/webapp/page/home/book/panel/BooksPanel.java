package by.stark.sample.webapp.page.home.book.panel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.Page;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.PageCreator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.PackageResourceReference;

import by.stark.sample.datamodel.Author;
import by.stark.sample.datamodel.Book;
import by.stark.sample.services.BookService;
import by.stark.sample.webapp.page.home.book.BookDetailsPage;
import by.stark.sample.webapp.page.home.book.BookPage;

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

		final PackageResourceReference resourceReference = new PackageResourceReference(
				BookPage.class, "images/" + book.getPicture().getName());
		add(new Image("image", resourceReference));

		add(new ListView<Author>("authors-list", allAuthors) {

			@Override
			protected void populateItem(ListItem<Author> item) {
				Author author = item.getModelObject();

				Link<Void> Link = new Link<Void>("linkToAuthor") {

					@Override
					public void onClick() {
						setResponsePage(new BookPage(author));
					}
				};

				item.add(Link);

				Link.add(new Label("author", new Model<String>(author
						.getFirstName().substring(0, 1)
						+ ". "
						+ author.getLastName())));

			}
		});

		add(new Label("pages", new Model<Long>(book.getPages())));
		add(new Label("publisher", new Model<String>(book.getPublisher()
				.getName())));
		add(new Label("year", new Model<Long>(book.getYear())));
		add(new Label("isbn", new Model<String>(book.getIsbn())));

		Link<Void> Link = new Link<Void>("linkToDetails") {

			@Override
			public void onClick() {
				setResponsePage(new BookDetailsPage(book, new PageCreator() {

					@Override
					public Page createPage() {
						return new BookPage();
					}
				}));
			}
		};
		add(Link);
		Link.add(new Label("title", new Model<String>(book.getTitle())));

	}
}
