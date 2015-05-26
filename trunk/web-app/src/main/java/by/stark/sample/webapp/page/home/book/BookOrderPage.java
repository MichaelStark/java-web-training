package by.stark.sample.webapp.page.home.book;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.joda.time.DateTime;

import by.stark.sample.datamodel.Book;
import by.stark.sample.datamodel.Libriary;
import by.stark.sample.datamodel.Record4Hands;
import by.stark.sample.datamodel.Record4Room;
import by.stark.sample.datamodel.Userprofile;
import by.stark.sample.datamodel.enums.RecordStatus;
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

	private Book book;
	private Userprofile user;

	private List<Long> choiceDays = new ArrayList<Long>();
	private List<Long> choiceTime = new ArrayList<Long>();
	private List<Long> choiceHours = new ArrayList<Long>();

	Boolean roomAv = true;
	Boolean handsAv = true;

	public BookOrderPage(Book book) {
		super(book.getTitle(), true);
		this.book = bookService.getById(book.getId());

		for (int i = 1; i < 31; i++) {
			choiceDays.add((long) i);
		}

		for (int i = getWorkstart(); i < getWorkfinish(); i++) {
			choiceTime.add((long) i);
		}
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

		final DropDownChoice<Long> time = new DropDownChoice<Long>("time",
				new Model<Long>(), choiceTime);
		roomForm.add(time.setRequired(true));

		final DropDownChoice<Long> hours = new DropDownChoice<Long>("hours",
				new Model<Long>(), choiceHours);
		roomForm.add(hours.setRequired(true).setOutputMarkupId(true));

		hours.add(new AjaxFormComponentUpdatingBehavior("change") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(feedback);
			}
		});

		time.add(new AjaxFormComponentUpdatingBehavior("change") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				int maxHours = getWorkfinish()
						- time.getModelObject().intValue();
				choiceHours.clear();
				for (int i = 1; i <= maxHours; i++) {
					choiceHours.add((long) i);
				}
				target.add(hours);
				target.add(feedback);
			}
		});

		roomForm.add(new AjaxButton("submitRoom") {
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.add(feedback);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				int start = time.getModelObject().intValue();
				int finish = start + hours.getModelObject().intValue();

				Date timeTake = new Date();
				Calendar c = Calendar.getInstance();
				c.set(c.HOUR_OF_DAY, start);
				c.set(c.MINUTE, 0);
				c.set(c.SECOND, 0);
				c.set(c.MILLISECOND, 0);
				timeTake = new DateTime(c.getTime()).plusDays(1).toDate();
				c.set(c.HOUR_OF_DAY, finish);
				Date timeReturn = new DateTime(c.getTime()).plusDays(1)
						.toDate();

				if (roomAv) {
					Libriary libraryAv = null;
					for (Libriary library : forRoom) {
						boolean wc = true;
						for (Record4Room record : record4RoomService
								.getAllByLibriary(library)) {
							if (record.getStatus() == RecordStatus.pending
									|| record.getStatus() == RecordStatus.taken
									|| record.getStatus() == RecordStatus.debt) {
								Date tempTake = record.getTimeTake();
								Date tempReturn = record.getTimeReturn();
								if ((timeTake.compareTo(tempReturn) >= 0)
										|| (timeReturn.compareTo(tempTake) <= 0)) {
									wc = true;
								} else {
									wc = false;
									break;
								}
							}
						}
						if (wc) {
							libraryAv = library;
							break;
						}
					}
					if (libraryAv != null) {
						Record4Room record = new Record4Room();

						record.setLibriary(libraryAv);
						record.setUser(user);
						record.setStatus(RecordStatus.pending);
						record.setTimeTake(timeTake);
						record.setTimeReturn(timeReturn);
						record4RoomService.saveOrUpdate(record);

						feedback.info(new ResourceModel(
								"p.home.book.order.orderComplete").getObject());
						roomForm.setVisible(false);
						target.add(feedback);
						target.add(form);
					} else {
						feedback.warn(new ResourceModel(
								"p.home.book.order.timeError").getObject());
						target.add(feedback);
					}
				}
			}

		});

		//
		Form handsForm = new Form("handsForm");
		add(handsForm.setEnabled(false)
				.add(new AttributeModifier("style", "display:none"))
				.setOutputMarkupId(true));

		final DropDownChoice<Long> days = new DropDownChoice<Long>("days",
				new Model<Long>(), choiceDays);
		handsForm.add(days.setRequired(true));

		handsForm.add(new AjaxButton("submitHands") {
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.add(feedback);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				if (handsAv) {
					Date dateTake = new Date();
					Calendar c = Calendar.getInstance();
					c.set(c.HOUR_OF_DAY, 0);
					c.set(c.MINUTE, 0);
					c.set(c.SECOND, 0);
					c.set(c.MILLISECOND, 0);
					dateTake = c.getTime();
					Date dateReturn = new DateTime(dateTake).plusDays(
							days.getModelObject().intValue() + 1).toDate();

					Record4Hands record = new Record4Hands();
					Libriary library = forHands.get(0);

					record.setLibriary(library);
					record.setUser(user);
					record.setStatus(RecordStatus.pending);
					record.setDateTake(dateTake);
					record.setDateReturn(dateReturn);
					record4HandsService.saveOrUpdate(record);

					library.setAvailability(false);
					libraryService.saveOrUpdate(library);

					feedback.info(new ResourceModel(
							"p.home.book.order.orderComplete").getObject());
					handsForm.setVisible(false);
					target.add(feedback);
					target.add(form);
				}
			}

		});
		//

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
