package by.stark.sample.webapp.page.home.book.author;

import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import by.stark.sample.datamodel.Author;
import by.stark.sample.services.AuthorService;
import by.stark.sample.webapp.page.home.HomePage;
import by.stark.sample.webapp.page.home.book.BookPage;

public class AuthorPage extends HomePage {

	@Inject
	private AuthorService authorService;

	List<Author> authors;

	public AuthorPage(String input) {
		super(input, false);
		authors = authorService.getAllByName(input);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		add(new Label("author-count", authors.size()));

		add(new ListView<Author>("authors-list", authors) {

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
						.getFirstName() + " " + author.getLastName())));

			}
		});
	}

	@Override
	protected IModel<String> getPageTitle() {
		return new ResourceModel("p.home.title");
	}
}
