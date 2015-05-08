package by.stark.sample.webapp.page.home.book;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.PageCreator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.resource.PackageResourceReference;

import by.stark.sample.datamodel.Author;
import by.stark.sample.datamodel.Book;
import by.stark.sample.datamodel.Genre;
import by.stark.sample.services.BookService;
import by.stark.sample.webapp.page.home.HomePage;

public class BookDetailsPage extends HomePage {

	@Inject
	private BookService bookService;
	private PageCreator pageCreator;
	Book book;

	public BookDetailsPage(Book book, PageCreator pageCreator) {
		super();
		this.book = book;
		this.pageCreator = pageCreator;
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		List<Author> allAuthors = new ArrayList<Author>();
		allAuthors.addAll(book.getAuthors());

		List<Genre> allGenres = new ArrayList<Genre>();
		allGenres.addAll(book.getGenres());

		add(new Link("back-link") {
			@Override
			public void onClick() {
				setResponsePage(pageCreator.createPage());
			}
		});

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
		add(new ListView<Genre>("genres-list", allGenres) {

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

				Link.add(new Label("genre", new Model<String>(genre.getName())));

			}
		});

		add(new Label("title", new Model<String>(book.getTitle())));
		add(new Label("pages", new Model<Long>(book.getPages())));
		add(new Label("publisher", new Model<String>(book.getPublisher()
				.getName())));
		add(new Label("year", new Model<Long>(book.getYear())));
		add(new Label("isbn", new Model<String>(book.getIsbn())));
		add(new Label("description", new Model<String>(book.getDescription())));
	}

	@Override
	protected IModel<String> getPageTitle() {
		return new ResourceModel("p.home.title");
	}

}
