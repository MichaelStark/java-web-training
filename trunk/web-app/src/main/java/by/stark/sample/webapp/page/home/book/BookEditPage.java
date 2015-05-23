package by.stark.sample.webapp.page.home.book;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.bean.validation.PropertyValidator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ResourceReference;

import by.stark.sample.datamodel.Author;
import by.stark.sample.datamodel.Book;
import by.stark.sample.datamodel.Ebook;
import by.stark.sample.datamodel.Genre;
import by.stark.sample.datamodel.Picture;
import by.stark.sample.datamodel.Publisher;
import by.stark.sample.services.AuthorService;
import by.stark.sample.services.BookService;
import by.stark.sample.services.EbookService;
import by.stark.sample.services.GenreService;
import by.stark.sample.services.PictureService;
import by.stark.sample.services.PublisherService;
import by.stark.sample.webapp.app.ImageResourceReference;
import by.stark.sample.webapp.page.home.HomePage;

import com.googlecode.wicket.kendo.ui.form.multiselect.MultiSelect;

public class BookEditPage extends HomePage {

	@Inject
	private AuthorService authorService;
	@Inject
	private GenreService genreService;
	@Inject
	private PictureService pictureService;
	@Inject
	private PublisherService publisherService;
	@Inject
	private BookService bookService;
	@Inject
	private EbookService ebookService;

	Book book;

	boolean newBook = false;

	public BookEditPage(Book book) {
		if (book.getId() != null) {
			this.book = bookService.getById(book.getId());
		} else {
			this.book = book;
			newBook = true;
		}

	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		final ResourceReference imagesResourceReference = new ImageResourceReference();
		final PageParameters imageParameters = new PageParameters();
		final PageParameters fileParameters = new PageParameters();

		Form<Book> form = new Form<Book>("form",
				new CompoundPropertyModel<Book>(book));
		this.add(form);

		if (!newBook) {
			String imageName = book.getPicture().getName();
			imageParameters.set("name", imageName);
			form.add(new Image("image", imagesResourceReference,
					imageParameters));
		} else {
			form.add(new Image("image", imagesResourceReference,
					imageParameters).add(new AttributeModifier("style",
					"display:none")));
		}

		final TextField<String> title = new TextField<String>("title");
		title.add(new PropertyValidator<String>());
		form.add(title);

		List<Genre> selectedGenres = new ArrayList<Genre>();
		if (!newBook) {
			selectedGenres.addAll(book.getGenres());
		}
		final MultiSelect<Genre> multiselectGenres = new MultiSelect<Genre>(
				"genres", new ListModel<Genre>(selectedGenres),
				new ListModel<Genre>(genreService.getAll()),
				new IChoiceRenderer<Genre>() {

					@Override
					public Object getDisplayValue(Genre object) {
						return object.getName();
					}

					@Override
					public String getIdValue(Genre object, int index) {
						return object.getId().toString();
					}

				});
		form.add(multiselectGenres);

		List<Author> selectedAuthors = new ArrayList<Author>();
		if (!newBook) {
			selectedAuthors.addAll(book.getAuthors());
		}
		final MultiSelect<Author> multiselectAuthors = new MultiSelect<Author>(
				"authors", new ListModel<Author>(selectedAuthors),
				new ListModel<Author>(authorService.getAll()),
				new IChoiceRenderer<Author>() {

					@Override
					public Object getDisplayValue(Author object) {
						return object.getFirstName() + " "
								+ object.getLastName();
					}

					@Override
					public String getIdValue(Author object, int index) {
						return object.getId().toString();
					}

				});
		form.add(multiselectAuthors);

		final TextField<String> isbn = new TextField<String>("isbn");
		isbn.add(new PropertyValidator<String>());
		form.add(isbn);

		final TextField<String> pages = new TextField<String>("pages");
		pages.add(new PropertyValidator<String>());
		form.add(pages);

		final TextField<Long> year = new TextField<Long>("year");
		year.add(new PropertyValidator<Long>());
		form.add(year);

		final TextArea<String> description = new TextArea<String>("description");
		description.add(new PropertyValidator<String>());
		form.add(description);

		DropDownChoice<Publisher> publisher = new DropDownChoice<Publisher>(
				"publisher", Model.of(book.getPublisher()),
				publisherService.getAll(), new IChoiceRenderer<Publisher>() {

					@Override
					public Object getDisplayValue(Publisher object) {
						return object.getName();
					}

					@Override
					public String getIdValue(Publisher object, int index) {
						return object.getId().toString();
					}
				});
		form.add(publisher);

		FileUploadField fileUploadField;
		FileUploadField imageUploadField;

		List<FileUpload> fileUpload = new ArrayList<FileUpload>();

		form.add(fileUploadField = new FileUploadField("fileUpload", new Model(
				(Serializable) fileUpload)));
		form.add(imageUploadField = new FileUploadField("imageUpload",
				new Model((Serializable) fileUpload)));

		AjaxButton save = new AjaxButton("submit") {

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				notification.error(target, "ERROR");
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> unused) {

				if (publisher.getModelObject() != null) {
					Picture picture = new Picture();
					final List<FileUpload> uploadsI = imageUploadField
							.getFileUploads();
					if (uploadsI != null) {
						for (FileUpload upload : uploadsI) {
							// Create a new file
							File newFile = new File(
									pictureService.getRootFolder(),
									upload.getClientFileName());
							try {
								if (!newBook) {
									picture = book.getPicture();
									File oldFile = new File(
											pictureService.getRootFolder(),
											book.getPicture().getName());
									oldFile.delete();
								}
								// Save to new file
								newFile.createNewFile();
								upload.writeTo(newFile);
								//
								picture.setName(upload.getClientFileName());
								book.setPicture(picture);
								pictureService.saveOrUpdate(picture);
							} catch (Exception e) {
								throw new IllegalStateException(
										"Unable to write file", e);
							}
						}
					}

					Set<Author> authors = new HashSet<Author>();
					authors.addAll(multiselectAuthors.getModelObject());
					Set<Genre> genres = new HashSet<Genre>();
					genres.addAll(multiselectGenres.getModelObject());

					book.setAuthors(authors);
					book.setGenres(genres);
					book.setPublisher(publisher.getModelObject());
					if (book.getPicture() != null) {
						bookService.saveOrUpdate(book);
						setResponsePage(new BookEditPage(book));
					}

					Ebook ebook = new Ebook();
					final List<FileUpload> uploads = fileUploadField
							.getFileUploads();
					if (uploads != null) {
						for (FileUpload upload : uploads) {
							// Create a new file
							File newFile = new File(
									ebookService.getRootFolder(),
									upload.getClientFileName());
							try {
								List<Ebook> ebooks = ebookService
										.getAllByBook(book);
								if (ebooks.size() == 1) {
									File oldFile = new File(
											ebookService.getRootFolder(),
											ebooks.get(0).getName());
									ebookService.delete(ebooks.get(0));
									oldFile.delete();
								}
								// Save to new file
								newFile.createNewFile();
								upload.writeTo(newFile);
								//
								ebook.setBook(book);
								ebook.setName(upload.getClientFileName());
								ebookService.saveOrUpdate(ebook);
							} catch (Exception e) {
								throw new IllegalStateException(
										"Unable to write file", e);
							}
						}
					}
				}
			}
		};
		AjaxButton cancel = new AjaxButton("cancel") {

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> unused) {
				setResponsePage(new BookPage());
			}
		};
		save.add(new AttributeModifier("value", new ResourceModel(
				"p.home.book.saveLink").getObject()));
		cancel.add(new AttributeModifier("value", new ResourceModel(
				"p.home.book.cancelLink").getObject()));
		form.add(save);
		form.add(cancel);

		if (!newBook) {
			List<Ebook> ebook = ebookService.getAllByBook(book);
			DownloadLink downloadLink = new DownloadLink("downloadLink",
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
			Label uploaded = new Label("uploaded", "");
			if (ebook.size() == 0) {
				downloadLink
						.add(new AttributeModifier("style", "display:none"));
				downloadLink.setEnabled(false);
			} else {
				uploaded = new Label("uploaded", ebook.get(0).getName());
			}
			form.add(downloadLink.add(uploaded));
		} else {
			Link downloadLink = new Link("downloadLink") {

				@Override
				public void onClick() {
				}

			};
			Label uploaded = new Label("uploaded", "");
			downloadLink.add(new AttributeModifier("style", "display:none"));
			downloadLink.setEnabled(false);
			form.add(downloadLink.add(uploaded));
		}

	}

	@Override
	protected IModel<String> getPageTitle() {
		return new ResourceModel("p.home.title");
	}
}
