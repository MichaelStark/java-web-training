package by.stark.sample.webapp.page.user;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

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
import by.stark.sample.services.UserService;
import by.stark.sample.webapp.app.BasicAuthenticationSession;
import by.stark.sample.webapp.page.BaseLayout;
import by.stark.sample.webapp.page.home.book.BookDetailsPage;
import by.stark.sample.webapp.page.login.LoginPage;
import by.stark.sample.webapp.page.user.provider.ProfileDataProviderRoom;

import com.googlecode.wicket.jquery.core.Options;
import com.googlecode.wicket.jquery.core.ajax.IJQueryAjaxAware;
import com.googlecode.wicket.jquery.core.ajax.JQueryAjaxBehavior;
import com.googlecode.wicket.jquery.ui.widget.dialog.DialogButton;
import com.googlecode.wicket.jquery.ui.widget.dialog.DialogButtons;
import com.googlecode.wicket.jquery.ui.widget.dialog.DialogIcon;
import com.googlecode.wicket.jquery.ui.widget.dialog.MessageDialog;
import com.googlecode.wicket.kendo.ui.datatable.DataTable;
import com.googlecode.wicket.kendo.ui.datatable.ToolbarAjaxBehavior;
import com.googlecode.wicket.kendo.ui.datatable.column.IColumn;
import com.googlecode.wicket.kendo.ui.datatable.column.PropertyColumn;

public class User4RoomPage extends BaseLayout {

	@Inject
	private LibriaryService libraryService;
	@Inject
	private BookService bookService;
	@Inject
	private Record4HandsService record4HandsService;
	@Inject
	private Record4RoomService record4RoomService;
	@Inject
	private UserService userService;

	private Long selectedId = null;
	private boolean roomType = false;
	private boolean handsType = false;

	public User4RoomPage() {
		super();
	};

	@Override
	protected void onInitialize() {
		super.onInitialize();

		if (!AuthenticatedWebSession.get().isSignedIn()) {
			setResponsePage(new LoginPage());
		}
		Userprofile user = BasicAuthenticationSession.get().getUser();

		final MessageDialog cancelDialog = new MessageDialog("warningDialog",
				new ResourceModel("p.user.dialog.title").getObject(),
				new ResourceModel("p.user.dialog.warning").getObject(),
				DialogButtons.YES_NO, DialogIcon.WARN) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target, DialogButton button) {
				if (button != null && button.match(LBL_YES)) {
					if (roomType && !handsType) {
						Record4Room record = record4RoomService.get(selectedId);
						record.setStatus(RecordStatus.canceled);
						record4RoomService.saveOrUpdate(record);
					} else if (!roomType && handsType) {
						Record4Hands record = record4HandsService
								.getById(selectedId);
						record.setStatus(RecordStatus.canceled);
						Libriary library = record.getLibriary();
						library.setAvailability(true);
						libraryService.saveOrUpdate(library);
						record4HandsService.saveOrUpdate(record);
					}
					setResponsePage(new User4RoomPage());
				}
				roomType = false;
				handsType = false;
			}
		};
		this.add(cancelDialog);

		add(new Link("linkToHands") {

			@Override
			public void onClick() {
				setResponsePage(new User4HandsPage());
			}

		});

		List<IColumn> columns = newColumnListRoom();

		Options options = new Options();
		options.set("height", 500);
		options.set("scrollable", "{ virtual: true }"); // infinite scroll
		options.set("columnMenu", true);
		options.set("groupable", true);
		options.set("selectable", true);
		options.set("toolbar", "[ { name: 'view', text: '"
				+ new ResourceModel("p.user.view").getObject()
				+ "' }, { name: 'cancel', text: '"
				+ new ResourceModel("p.user.cancel").getObject() + "' } ]");

		List<Record4Room> records = new ArrayList<Record4Room>();
		for (Record4Room record : record4RoomService.getAllByUser(user)) {
			Record4Room rec = record4RoomService.getById(record.getId());
			Libriary lib = libraryService.getById(rec.getLibriary().getId());
			rec.setLibriary(lib);
			records.add(rec);
		}

		IDataProvider<Record4Room> provider = new ProfileDataProviderRoom(
				records);
		final DataTable<Record4Room> table = new DataTable<Record4Room>(
				"datatable", columns, provider, 20, options) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target, String button,
					List<String> values) {
				if (button.equals("view") && !values.get(0).equals("")) {
					Book book = bookService.getById(libraryService
							.getById(
									record4RoomService
											.getById(
													Long.parseLong(values
															.get(0)))
											.getLibriary().getId()).getBook()
							.getId());
					setResponsePage(new BookDetailsPage(book));
				} else if (button.equals("cancel") && !values.get(0).equals("")) {
					selectedId = Long.parseLong(values.get(0));
					roomType = true;
					if (record4RoomService.get(selectedId).getStatus() == RecordStatus.pending) {
						cancelDialog.open(target);
					}
				}
			}

			@Override
			protected JQueryAjaxBehavior newToolbarAjaxBehavior(
					IJQueryAjaxAware source) {
				return new ToolbarAjaxBehavior(source, "id");
			}
		};
		this.add(table);

	}

	private static List<IColumn> newColumnListRoom() {
		List<IColumn> columns = new ArrayList<IColumn>();

		columns.add(new PropertyColumn("ID", "id", 50));
		columns.add(new PropertyColumn(new ResourceModel("p.user.title")
				.getObject(), "libriary.book.title"));
		columns.add(new PropertyColumn(new ResourceModel("p.user.take")
				.getObject(), "timeTake"));
		columns.add(new PropertyColumn(new ResourceModel("p.user.return")
				.getObject(), "timeReturn"));
		columns.add(new PropertyColumn(new ResourceModel("p.user.status")
				.getObject(), "status"));

		return columns;

	}

	@Override
	protected IModel<String> getPageTitle() {
		return new ResourceModel("p.home.title");
	}

}
