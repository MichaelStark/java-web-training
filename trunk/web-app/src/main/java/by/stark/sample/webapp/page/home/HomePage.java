package by.stark.sample.webapp.page.home;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.core.util.lang.PropertyResolver;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import by.stark.sample.datamodel.Author;
import by.stark.sample.datamodel.Book;
import by.stark.sample.datamodel.Genre;
import by.stark.sample.services.AuthorService;
import by.stark.sample.services.BookService;
import by.stark.sample.services.GenreService;
import by.stark.sample.webapp.page.BaseLayout;
import by.stark.sample.webapp.page.home.book.BookDetailsPage;
import by.stark.sample.webapp.page.home.book.BookPage;
import by.stark.sample.webapp.page.home.book.author.AuthorPage;

import com.googlecode.wicket.jquery.core.renderer.TextRenderer;
import com.googlecode.wicket.jquery.ui.form.autocomplete.AutoCompleteTextField;
import com.googlecode.wicket.kendo.ui.widget.notification.Notification;

public class HomePage extends BaseLayout {

	@Inject
	private GenreService genreService;

	@Inject
	private BookService bookService;

	@Inject
	private AuthorService authorService;

	public final static Notification notification = new Notification(
			"notification");

	private List<String> searchType = Arrays.asList(new ResourceModel(
			"p.home.searchSelectTitle").getObject(), new ResourceModel(
			"p.home.searchSelectAuthor").getObject());

	String selectedGenre;

	String input;

	boolean byTitle = true;

	List<Author> choicesAuthors = new ArrayList<Author>();
	List<Book> choicesBooks = new ArrayList<Book>();

	public HomePage() {
		super();
	};

	public HomePage(String selectedGenre) {
		this.selectedGenre = selectedGenre;
	}

	public HomePage(String input, boolean byTitle) {
		super();
		this.input = input;
		this.byTitle = byTitle;

	};

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
				if (genre.getName().equals(selectedGenre)) {
					Link.add(new AttributeModifier("class", "selected_link"));
				}
				Link.add(new Label("genre", genre.getName()));

			}
		});

		final Form form = new Form("searchForm");

		this.add(notification);

		this.add(form);

		AutoCompleteTextField<Book> autocompleteTitle = new AutoCompleteTextField<Book>(
				"searchTextTitle", new Model<Book>(), new TextRenderer<Book>(
						"title"), Book.class) {

			private static final long serialVersionUID = 1L;

			@Override
			protected List<Book> getChoices(String input) {
				List<Book> choices = new ArrayList<Book>();

				for (Book book : bookService.getAllByTitleWithSortAndPagging(
						input, 0, bookService.getAll().size())) {

					choices.add(book);
				}
				HomePage.this.input = input;
				HomePage.this.choicesBooks = choices;
				return choices;
			}
		};
		autocompleteTitle.add(new AttributeModifier("placeholder",
				new ResourceModel("p.home.searchPlaceholder")));
		autocompleteTitle.setOutputMarkupId(true);
		form.add(autocompleteTitle.setRequired(true));

		AutoCompleteTextField<Author> autocompleteAuthor = new AutoCompleteTextField<Author>(
				"searchTextAuthor", new Model<Author>(),
				new TextRenderer<Author>("firstName") {
					@Override
					public String getText(Author object, String expression) {
						if (expression != null) {

							Object value1 = PropertyResolver.getValue(
									expression, object);

							String expression2 = "lastName";
							Object value2 = PropertyResolver.getValue(
									expression2, object);

							if (value1 != null) {
								return value1.toString() + " "
										+ value2.toString();
							}
						}
						return "";
					}
				}, Author.class) {

			private static final long serialVersionUID = 1L;

			@Override
			protected List<Author> getChoices(String input) {
				List<Author> choices = new ArrayList<Author>();

				for (Author author : authorService.getAllByName(input)) {

					choices.add(author);
				}
				HomePage.this.input = input;
				HomePage.this.choicesAuthors = choices;
				return choices;
			}
		};
		autocompleteAuthor.add(new AttributeModifier("placeholder",
				new ResourceModel("p.home.searchPlaceholder")));
		autocompleteAuthor.setOutputMarkupId(true);
		form.add(autocompleteAuthor.setRequired(true));

		DropDownChoice<String> dropdown = new DropDownChoice<String>(
				"searchSelect", byTitle ? Model.of(searchType.get(0))
						: Model.of(searchType.get(1)), searchType);
		form.add(dropdown);

		if (byTitle) {
			autocompleteAuthor.add(new AttributeModifier("style",
					"display:none"));
			autocompleteAuthor.setEnabled(false);
			autocompleteTitle.add(new AttributeModifier("value", input));
		} else {
			autocompleteTitle
					.add(new AttributeModifier("style", "display:none"));
			autocompleteTitle.setEnabled(false);
			autocompleteAuthor.add(new AttributeModifier("value", input));
		}

		dropdown.add(new AjaxFormComponentUpdatingBehavior("change") {

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				if (dropdown.getModelObject().equals(searchType.get(0))) {
					autocompleteAuthor.add(new AttributeModifier("style",
							"display:none"));
					autocompleteTitle.add(new AttributeModifier("style",
							"display:block"));
					autocompleteTitle.setEnabled(true);
					autocompleteAuthor.setEnabled(false);
					autocompleteTitle
							.add(new AttributeModifier("value", input));
				} else {
					autocompleteAuthor.add(new AttributeModifier("style",
							"display:block"));
					autocompleteTitle.add(new AttributeModifier("style",
							"display:none"));
					autocompleteTitle.setEnabled(false);
					autocompleteAuthor.setEnabled(true);
					autocompleteAuthor
							.add(new AttributeModifier("value", input));
				}
				target.add(autocompleteAuthor);
				target.add(autocompleteTitle);
			}
		});

		AjaxButton submit = new AjaxButton("searchSubmit") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				notification.error(target, new ResourceModel(
						"p.home.searchNotification").getObject());
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> unused) {
				if (dropdown.getModelObject().equals(searchType.get(0))) {
					Book book = autocompleteTitle.getModelObject();
					if (book != null) {
						setResponsePage(new BookDetailsPage(book));
					} else if (choicesBooks.size() == 1) {
						setResponsePage(new BookDetailsPage(choicesBooks.get(0)));
					} else {
						setResponsePage(new BookPage(input));
					}

				} else {
					Author author = autocompleteAuthor.getModelObject();
					if (author != null) {
						setResponsePage(new BookPage(author));
					} else if (choicesAuthors.size() == 1) {
						setResponsePage(new BookPage(choicesAuthors.get(0)));
					} else {
						setResponsePage(new AuthorPage(input));
					}

				}
			}
		};
		form.add(submit);
		submit.add(new AttributeModifier("value", new ResourceModel(
				"p.home.searchLink")));
	}

	@Override
	protected IModel<String> getPageTitle() {
		return new ResourceModel("p.home.title");
	}

}
