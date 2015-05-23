package by.stark.sample.webapp.page.home.book;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ResourceReference;

import by.stark.sample.datamodel.Author;
import by.stark.sample.datamodel.Book;
import by.stark.sample.datamodel.Ebook;
import by.stark.sample.datamodel.Genre;
import by.stark.sample.services.BookService;
import by.stark.sample.services.EbookService;
import by.stark.sample.services.PictureService;
import by.stark.sample.webapp.app.ImageResourceReference;
import by.stark.sample.webapp.page.home.HomePage;

public class BookDetailsPage extends HomePage {

	@Inject
	private BookService bookService;
	@Inject
	private EbookService ebookService;
	@Inject
	private PictureService pictureService;

	Book book;

	public BookDetailsPage(Book book) {
		super(book.getTitle(), true);
		this.book = bookService.getById(book.getId());
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		final ResourceReference imagesResourceReference = new ImageResourceReference();
		final PageParameters imageParameters = new PageParameters();
		final PageParameters fileParameters = new PageParameters();

		List<Author> allAuthors = new ArrayList<Author>();
		allAuthors.addAll(book.getAuthors());

		List<Genre> allGenres = new ArrayList<Genre>();
		allGenres.addAll(book.getGenres());

		String imageName = book.getPicture().getName();
		imageParameters.set("name", imageName);
		add(new Image("image", imagesResourceReference, imageParameters));

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

		Link<Void> Edit = new Link<Void>("linkToEdit") {

			@Override
			public void onClick() {
				setResponsePage(new BookEditPage(book));
			}
		};
		add(Edit);

		Link<Void> Delete = new Link<Void>("linkToDelete") {

			@Override
			public void onClick() {
				if (ebookService.getAllByBook(book).size() > 0) {
					Ebook ebook = ebookService.getAllByBook(book).get(0);
					File oldFile = new File(ebookService.getRootFolder(),
							ebook.getName());
					oldFile.delete();
					ebookService.delete(ebook);
				}
				File oldFile = new File(pictureService.getRootFolder(), book
						.getPicture().getName());
				oldFile.delete();

				bookService.delete(book);
				pictureService.delete(book.getPicture());

				setResponsePage(new BookPage());
			}
		};
		add(Delete);
		add(new Link<Void>("linkToAddBook") {
			@Override
			public void onClick() {
				setResponsePage(new BookEditPage(new Book()));
			}
		});

		List<Ebook> ebook = ebookService.getAllByBook(book);
		DownloadLink downloadLink = new DownloadLink("linkToDownload",
				new AbstractReadOnlyModel<File>() {
					private static final long serialVersionUID = 1L;

					@Override
					public File getObject() {
						File tempFile;
						tempFile = new File(ebookService.getRootFolder()
								+ ebook.get(0).getName());
						return tempFile;
					}
				});
		if (ebook.size() == 0) {
			downloadLink.add(new AttributeModifier("style", "display:none"));
			downloadLink.setEnabled(false);
		}
		add(downloadLink);
	}

	@Override
	protected IModel<String> getPageTitle() {
		return new ResourceModel("p.home.title");
	}

}
