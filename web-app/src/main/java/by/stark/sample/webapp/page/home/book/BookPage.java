package by.stark.sample.webapp.page.home.book;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.metamodel.SingularAttribute;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ResourceReference;

import by.stark.sample.datamodel.Author;
import by.stark.sample.datamodel.Book;
import by.stark.sample.datamodel.Ebook;
import by.stark.sample.datamodel.Genre;
import by.stark.sample.datamodel.Libriary;
import by.stark.sample.services.BookService;
import by.stark.sample.services.EbookService;
import by.stark.sample.services.LibriaryService;
import by.stark.sample.services.PictureService;
import by.stark.sample.webapp.app.ImageResourceReference;
import by.stark.sample.webapp.page.home.HomePage;
import by.stark.sample.webapp.page.home.book.panel.MyPagingNavigator;

import com.googlecode.wicket.jquery.ui.widget.dialog.DialogButton;
import com.googlecode.wicket.jquery.ui.widget.dialog.DialogButtons;
import com.googlecode.wicket.jquery.ui.widget.dialog.DialogIcon;
import com.googlecode.wicket.jquery.ui.widget.dialog.MessageDialog;

public class BookPage extends HomePage {

	@Inject
	private BookService bookService;
	@Inject
	private EbookService ebookService;
	@Inject
	private PictureService pictureService;
	@Inject
	private LibriaryService libraryService;

	int typeOfSearch = 0;
	final int size;
	int booksPerPage = 10;
	String title;
	Author author;
	Genre genre;

	Book selectedBook;

	public BookPage() {
		size = bookService.getAll().size();
		typeOfSearch = 0;
	}

	public BookPage(Author author) {
		super(author.getFirstName() + " " + author.getLastName(), false);
		this.author = author;
		size = bookService.getAllByAuthor(author).size();
		typeOfSearch = 1;
	}

	public BookPage(Genre genre) {
		super(genre.getName());
		this.genre = genre;
		size = bookService.getAllByGenre(genre).size();
		typeOfSearch = 2;
	}

	public BookPage(String title) {
		super(title, true);
		this.title = title;
		size = bookService.getAllByTitleCount(title);
		typeOfSearch = 3;
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		final MessageDialog deleteDialog = new MessageDialog("warningDialog",
				new ResourceModel("p.admin.deleteWarning").getObject(),
				new ResourceModel("p.admin.deleteDialog").getObject(),
				DialogButtons.YES_NO, DialogIcon.WARN) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target, DialogButton button) {
				if (button != null && button.match(LBL_YES)) {
					if (ebookService.getAllByBook(selectedBook).size() > 0) {
						Ebook ebook = ebookService.getAllByBook(selectedBook)
								.get(0);
						File oldFile = new File(ebookService.getRootFolder(),
								ebook.getName());
						oldFile.delete();
						ebookService.delete(ebook);
					}
					File oldFile = new File(pictureService.getRootFolder(),
							selectedBook.getPicture().getName());
					oldFile.delete();

					List<Libriary> librarys = libraryService
							.getAllByBook(selectedBook);
					for (Libriary library : librarys) {
						libraryService.delete(library);
					}
					bookService.delete(selectedBook);
					pictureService.delete(selectedBook.getPicture());

					switch (typeOfSearch) {
					case 0:
						setResponsePage(new BookPage());
						break;
					case 1:
						setResponsePage(new BookPage(author));
						break;
					case 2:
						setResponsePage(new BookPage(genre));
						break;
					case 3:
						setResponsePage(new BookPage(title));
						break;
					default:
						setResponsePage(new BookPage());
						break;
					}
				}
				selectedBook = null;
			}
		};
		add(deleteDialog);

		final ResourceReference imagesResourceReference = new ImageResourceReference();

		BookDataProvider bookDataProvider = new BookDataProvider();

		DataView<Book> dataView = new DataView<Book>("books-list",
				bookDataProvider, booksPerPage) {
			@Override
			protected void populateItem(Item<Book> item) {
				final PageParameters imageParameters = new PageParameters();
				final PageParameters fileParameters = new PageParameters();
				final Book book = bookService.getById(item.getModelObject()
						.getId());

				String imageName = book.getPicture().getName();
				imageParameters.set("name", imageName);

				item.add(new Image("image", imagesResourceReference,
						imageParameters));

				List<Author> allAuthors = new ArrayList<Author>();
				allAuthors.addAll(book.getAuthors());

				item.add(new ListView<Author>("authors-list", allAuthors) {

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

				item.add(new Label("pages"));
				item.add(new Label("publisher", new Model<String>(book
						.getPublisher().getName())));
				item.add(new Label("year"));
				item.add(new Label("isbn"));

				Link<Void> Details = new Link<Void>("linkToDetails") {

					@Override
					public void onClick() {
						setResponsePage(new BookDetailsPage(book));
					}
				};
				item.add(Details);
				Details.add(new Label("title"));

				Link Order = new Link("linkToOrder") {
					@Override
					public void onClick() {
						setResponsePage(new BookOrderPage(book));

					}
				};
				item.add(Order);

				SecuredLinkForAdmin Edit = new SecuredLinkForAdmin("linkToEdit") {

					@Override
					public void onClick() {
						setResponsePage(new BookEditPage(book));
					}
				};
				item.add(Edit);

				SecuredAjaxLinkForAdmin Delete = new SecuredAjaxLinkForAdmin(
						"linkToDelete") {

					@Override
					public void onClick(AjaxRequestTarget target) {
						selectedBook = book;
						deleteDialog.open(target);

					}
				};
				item.add(Delete);

				List<Ebook> ebook = ebookService.getAllByBook(book);
				DownloadLink downloadLink = new DownloadLink("linkToDownload",
						new AbstractReadOnlyModel<File>() {
							private static final long serialVersionUID = 1L;

							@Override
							public File getObject() {
								File tempFile;
								tempFile = new File(
										ebookService.getRootFolder()
												+ ebook.get(0).getName());
								return tempFile;
							}
						});
				if (ebook.size() == 0) {
					downloadLink.add(new AttributeModifier("style",
							"display:none"));
					downloadLink.setEnabled(false);
				}
				item.add(downloadLink);

			}
		};

		add(dataView);
		add(new MyPagingNavigator("paging", dataView));
		add(new Label("books-count", size));
		add(new SecuredLinkForAdmin("linkToAddBook") {
			@Override
			public void onClick() {
				setResponsePage(new BookEditPage(new Book()));
			}
		});

	}

	private class BookDataProvider extends
			SortableDataProvider<Book, SingularAttribute<Book, ?>> {

		public BookDataProvider() {
			super();
		}

		@Override
		public Iterator<? extends Book> iterator(long first, long count) {
			if (typeOfSearch == 1) {
				return bookService.getAllByAuthorWithSortAndPagging(author,
						(int) first, (int) count).iterator();
			} else if (typeOfSearch == 2) {
				return bookService.getAllByGenreWithSortAndPagging(genre,
						(int) first, (int) count).iterator();
			} else if (typeOfSearch == 3) {
				return bookService.getAllByTitleWithSortAndPagging(title,
						(int) first, (int) count).iterator();
			}
			return bookService.getAllWithSortAndPagging((int) first,
					(int) count).iterator();
		}

		@Override
		public long size() {
			return size;
		}

		@Override
		public IModel<Book> model(Book book) {
			return new CompoundPropertyModel<Book>(book);
		}
	}
}
