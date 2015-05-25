package by.stark.sample.webapp.page.home.book;

import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import by.stark.sample.datamodel.Book;
import by.stark.sample.datamodel.Libriary;
import by.stark.sample.datamodel.Userprofile;
import by.stark.sample.services.BookService;
import by.stark.sample.services.LibriaryService;
import by.stark.sample.services.Record4HandsService;
import by.stark.sample.services.Record4RoomService;
import by.stark.sample.webapp.app.BasicAuthenticationSession;
import by.stark.sample.webapp.page.home.HomePage;
import by.stark.sample.webapp.page.login.LoginPage;

import com.googlecode.wicket.jquery.ui.panel.JQueryFeedbackPanel;

public class BookOrderPage extends HomePage {

	@Inject
	private BookService bookService;
	@Inject
	private Record4HandsService record4HandsService;
	@Inject
	private Record4RoomService record4RoomService;
	@Inject
	private LibriaryService libraryService;

	Book book;
	Userprofile user;

	Boolean roomAv = true;
	Boolean handsAv = true;

	public BookOrderPage(Book book) {
		super(book.getTitle(), true);
		this.book = bookService.getById(book.getId());
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		if (!AuthenticatedWebSession.get().isSignedIn()) {
			setResponsePage(new LoginPage());
		}
		user = BasicAuthenticationSession.get().getUser();

		final FeedbackPanel feedback = new JQueryFeedbackPanel("feedback");
		add(feedback.setOutputMarkupId(true));

		List<Libriary> forRoom = libraryService.getAllLibriarysByBook(book,
				true, true);
		List<Libriary> forHands = libraryService.getAllLibriarysByBook(book,
				true, false);

		if (forRoom.size() == 0 && forHands.size() == 0) {
			feedback.warn(new ResourceModel("p.home.book.order.infoNot4Order")
					.getObject());
			roomAv = false;
			handsAv = false;
		} else if (forRoom.size() != 0 && forHands.size() == 0) {
			feedback.warn(new ResourceModel("p.home.book.order.info4Room")
					.getObject());
			roomAv = true;
			handsAv = false;
		} else if (forRoom.size() == 0 && forHands.size() != 0) {
			feedback.warn(new ResourceModel("p.home.book.order.info4Hands")
					.getObject());
			roomAv = false;
			handsAv = true;
		}

		add(new Label("title", new Model<String>(book.getTitle())));

		Form roomForm = new Form("roomForm");
		add(roomForm.setEnabled(false)
				.add(new AttributeModifier("style", "display:none"))
				.setOutputMarkupId(true));

		Form handsForm = new Form("handsForm");
		add(handsForm.setEnabled(false)
				.add(new AttributeModifier("style", "display:none"))
				.setOutputMarkupId(true));

		AjaxLink Room = new AjaxLink("linkToRoom") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				if (roomAv) {
					handsForm.setEnabled(false).add(
							new AttributeModifier("style", "display:none"));
					roomForm.setEnabled(true).add(
							new AttributeModifier("style", "display:block"));
					target.add(roomForm);
					target.add(handsForm);
					target.add(feedback);
				}
			}
		};
		add(Room.setVisible(roomAv));

		AjaxLink Hands = new AjaxLink("linkToHands") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				if (handsAv) {
					handsForm.setEnabled(true).add(
							new AttributeModifier("style", "display:block"));
					roomForm.setEnabled(false).add(
							new AttributeModifier("style", "display:none"));
					target.add(roomForm);
					target.add(handsForm);
					target.add(feedback);
				}
			}
		};
		add(Hands.setVisible(handsAv));
	}

	@Override
	protected IModel<String> getPageTitle() {
		return new ResourceModel("p.home.title");
	}

}
