package by.stark.sample.webapp.page.librarian.debts;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import by.stark.sample.datamodel.Book;
import by.stark.sample.datamodel.Libriary;
import by.stark.sample.datamodel.Record4Room;
import by.stark.sample.datamodel.enums.RecordStatus;
import by.stark.sample.services.BookService;
import by.stark.sample.services.LibriaryService;
import by.stark.sample.services.Record4RoomService;
import by.stark.sample.services.UserService;
import by.stark.sample.webapp.page.BaseLayout;
import by.stark.sample.webapp.page.home.book.BookDetailsPage;
import by.stark.sample.webapp.page.librarian.Librarian4RoomPage;
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

@AuthorizeInstantiation({ "librarian" })
public class Debts4RoomPage extends BaseLayout {

	@Inject
	private LibriaryService libraryService;
	@Inject
	private BookService bookService;
	@Inject
	private Record4RoomService record4RoomService;
	@Inject
	private UserService userService;

	private List<String> selectedValues = new ArrayList<String>();

	public Debts4RoomPage() {
		super();
	};

	@Override
	protected void onInitialize() {
		super.onInitialize();

		final MessageDialog checkDialog = new MessageDialog("dialog",
				new ResourceModel("p.user.dialog.title").getObject(),
				new ResourceModel("p.librarian.complete").getObject(),
				DialogButtons.YES_NO, DialogIcon.WARN) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target, DialogButton button) {
				if (button != null && button.match(LBL_YES)) {
					for (String value : selectedValues) {
						Record4Room record = record4RoomService.getById(Long
								.parseLong(value));
						record.setStatus(RecordStatus.completed);
						record4RoomService.saveOrUpdate(record);
					}
					setResponsePage(new Debts4RoomPage());
				}
			}
		};
		this.add(checkDialog);

		add(new Link("linkToAll") {

			@Override
			public void onClick() {
				setResponsePage(new Librarian4RoomPage());
			}

		});

		List<IColumn> columns = newColumnListRoom();

		Options options = new Options();
		options.set("height", 500);
		options.set("scrollable", "{ virtual: true }"); // infinite scroll
		options.set("columnMenu", true);
		options.set("groupable", true);
		options.set("selectable", Options.asString("multiple"));
		options.set("toolbar", "[ { name: 'view', text: '"
				+ new ResourceModel("p.librarian.view").getObject()
				+ "' }, { name: 'save', text: '"
				+ new ResourceModel("p.librarian.complete").getObject()
				+ "' } ]");

		Date curTime = Calendar.getInstance().getTime();

		List<Record4Room> records = new ArrayList<Record4Room>();
		for (Record4Room record : record4RoomService
				.getAllByStatus(RecordStatus.debt)) {
			Record4Room rec = record4RoomService.getById(record.getId());
			Libriary lib = libraryService.getById(rec.getLibriary().getId());
			rec.setLibriary(lib);
			records.add(rec);
		}
		for (Record4Room record : record4RoomService
				.getAllByStatus(RecordStatus.taken)) {
			if (record.getTimeReturn().compareTo(curTime) <= 0) {
				record.setStatus(RecordStatus.debt);
				record4RoomService.saveOrUpdate(record);

				Record4Room rec = record4RoomService.getById(record.getId());
				Libriary lib = libraryService
						.getById(rec.getLibriary().getId());
				rec.setLibriary(lib);
				records.add(rec);
			}
		}

		IDataProvider<Record4Room> provider = new ProfileDataProviderRoom(
				records);
		final DataTable<Record4Room> table = new DataTable<Record4Room>(
				"datatable", columns, provider, 20, options) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target, String button,
					List<String> values) {
				if (button.equals("view") && !values.get(0).equals("")
						&& values.size() == 1) {
					Book book = bookService.getById(libraryService
							.getById(
									record4RoomService
											.getById(
													Long.parseLong(values
															.get(0)))
											.getLibriary().getId()).getBook()
							.getId());
					setResponsePage(new BookDetailsPage(book));
				} else if (button.equals("save") && !values.get(0).equals("")) {
					selectedValues = values;
					checkDialog.open(target);
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
		columns.add(new PropertyColumn(new ResourceModel("p.librarian.user")
				.getObject(), "user.email"));
		columns.add(new PropertyColumn(new ResourceModel("p.librarian.title")
				.getObject(), "libriary.book.title"));
		columns.add(new PropertyColumn(new ResourceModel("p.librarian.take")
				.getObject(), "timeTake"));
		columns.add(new PropertyColumn(new ResourceModel("p.librarian.return")
				.getObject(), "timeReturn"));
		columns.add(new PropertyColumn(new ResourceModel("p.librarian.status")
				.getObject(), "status"));

		return columns;

	}

	@Override
	protected IModel<String> getPageTitle() {
		return new ResourceModel("p.home.title");
	}

}
