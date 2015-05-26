package by.stark.sample.webapp.page.librarian;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import by.stark.sample.datamodel.Book;
import by.stark.sample.datamodel.Libriary;
import by.stark.sample.datamodel.Record4Hands;
import by.stark.sample.datamodel.Userprofile;
import by.stark.sample.datamodel.enums.RecordStatus;
import by.stark.sample.services.BookService;
import by.stark.sample.services.LibriaryService;
import by.stark.sample.services.Record4HandsService;
import by.stark.sample.services.UserService;
import by.stark.sample.webapp.page.BaseLayout;
import by.stark.sample.webapp.page.home.book.BookDetailsPage;
import by.stark.sample.webapp.page.librarian.debts.Debts4HandsPage;
import by.stark.sample.webapp.page.user.provider.ProfileDataProviderHands;

import com.googlecode.wicket.jquery.core.Options;
import com.googlecode.wicket.jquery.core.ajax.IJQueryAjaxAware;
import com.googlecode.wicket.jquery.core.ajax.JQueryAjaxBehavior;
import com.googlecode.wicket.jquery.ui.JQueryIcon;
import com.googlecode.wicket.jquery.ui.form.RadioChoice;
import com.googlecode.wicket.jquery.ui.panel.JQueryFeedbackPanel;
import com.googlecode.wicket.jquery.ui.widget.dialog.AbstractFormDialog;
import com.googlecode.wicket.jquery.ui.widget.dialog.DialogButton;
import com.googlecode.wicket.kendo.ui.datatable.DataTable;
import com.googlecode.wicket.kendo.ui.datatable.ToolbarAjaxBehavior;
import com.googlecode.wicket.kendo.ui.datatable.column.DatePropertyColumn;
import com.googlecode.wicket.kendo.ui.datatable.column.IColumn;
import com.googlecode.wicket.kendo.ui.datatable.column.PropertyColumn;

@AuthorizeInstantiation({ "librarian" })
public class Librarian4HandsPage extends BaseLayout {

	@Inject
	private LibriaryService libraryService;
	@Inject
	private BookService bookService;
	@Inject
	private Record4HandsService record4HandsService;
	@Inject
	private UserService userService;

	public Librarian4HandsPage() {
		super();
	};

	@Override
	protected void onInitialize() {
		super.onInitialize();

		final LibrarianDialog dialog = new LibrarianDialog("dialog",
				"record details") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit(AjaxRequestTarget target) {
				Record4Hands record = this.getModelObject();

				record4HandsService.saveOrUpdate(record);
				if (record.getStatus() == RecordStatus.canceled
						|| record.getStatus() == RecordStatus.completed
						|| record.getStatus() == RecordStatus.returned) {
					Libriary library = record.getLibriary();
					library.setAvailability(true);
					libraryService.saveOrUpdate(library);
				} else if (record.getStatus() == RecordStatus.pending
						|| record.getStatus() == RecordStatus.taken
						|| record.getStatus() == RecordStatus.debt) {
					Libriary library = record.getLibriary();
					library.setAvailability(false);
					libraryService.saveOrUpdate(library);
				}
				setResponsePage(new Librarian4HandsPage());
			}

		};
		this.add(dialog);

		add(new Link("linkToDebt") {

			@Override
			public void onClick() {
				setResponsePage(new Debts4HandsPage());
			}

		});

		Options options = new Options();
		options.set("height", 500);
		options.set("scrollable", "{ virtual: true }"); // infinite scroll
		options.set("columnMenu", true);
		options.set("groupable", true);
		options.set("selectable", true);
		options.set("toolbar", "[ { name: 'view', text: '"
				+ new ResourceModel("p.librarian.view").getObject()
				+ "' }, { name: 'edit', text: '"
				+ new ResourceModel("p.librarian.edit").getObject() + "' } ]");

		List<IColumn> columns2 = newColumnListHands();

		List<Record4Hands> records2 = new ArrayList<Record4Hands>();
		for (Record4Hands record : record4HandsService
				.getAllByStatus(RecordStatus.pending)) {
			Record4Hands rec = record4HandsService.getById(record.getId());
			Libriary lib = libraryService.getById(rec.getLibriary().getId());
			rec.setLibriary(lib);
			records2.add(rec);
		}
		for (Record4Hands record : record4HandsService
				.getAllByStatus(RecordStatus.taken)) {
			Record4Hands rec = record4HandsService.getById(record.getId());
			Libriary lib = libraryService.getById(rec.getLibriary().getId());
			rec.setLibriary(lib);
			records2.add(rec);
		}

		IDataProvider<Record4Hands> provider2 = new ProfileDataProviderHands(
				records2);
		final DataTable<Record4Hands> table2 = new DataTable<Record4Hands>(
				"datatable2", columns2, provider2, 20, options) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target, String button,
					List<String> values) {
				if (button.equals("view") && !values.get(0).equals("")) {
					Book book = bookService.getById(libraryService
							.getById(
									record4HandsService
											.getById(
													Long.parseLong(values
															.get(0)))
											.getLibriary().getId()).getBook()
							.getId());
					setResponsePage(new BookDetailsPage(book));
				} else if (button.equals("edit") && !values.get(0).equals("")) {
					Record4Hands record = record4HandsService.getById(Long
							.parseLong(values.get(0)));

					dialog.setTitle(target, new ResourceModel(
							"p.librarian.dialog.title").getObject()
							+ " "
							+ record.getId());
					dialog.setModelObject(record);
					dialog.open(target);
				}
			}

			@Override
			protected JQueryAjaxBehavior newToolbarAjaxBehavior(
					IJQueryAjaxAware source) {
				return new ToolbarAjaxBehavior(source, "id");
			}
		};
		this.add(table2);

	}

	private static List<IColumn> newColumnListHands() {
		List<IColumn> columns = new ArrayList<IColumn>();

		columns.add(new PropertyColumn("ID", "id", 50));
		columns.add(new PropertyColumn(new ResourceModel("p.librarian.user")
				.getObject(), "user.email"));
		columns.add(new PropertyColumn(new ResourceModel("p.librarian.title")
				.getObject(), "libriary.book.title"));
		columns.add(new DatePropertyColumn(
				new ResourceModel("p.librarian.take").getObject(), "dateTake"));
		columns.add(new DatePropertyColumn(new ResourceModel(
				"p.librarian.return").getObject(), "dateReturn"));
		columns.add(new PropertyColumn(new ResourceModel("p.librarian.status")
				.getObject(), "status"));

		return columns;

	}

	abstract static class LibrarianDialog extends
			AbstractFormDialog<Record4Hands> {
		private static final long serialVersionUID = 1L;
		protected final DialogButton btnSubmit = new DialogButton(
				new ResourceModel("p.admin.dialog.save").getObject(),
				JQueryIcon.CHECK);
		protected final DialogButton btnCancel = new DialogButton(
				new ResourceModel("p.admin.dialog.cancel").getObject(),
				JQueryIcon.CANCEL);

		private Form<?> form;
		private FeedbackPanel feedback;

		public LibrarianDialog(String id, String title) {
			super(id, title, true);

			this.form = new Form<Record4Hands>("form",
					new CompoundPropertyModel<Record4Hands>(this.getModel()));
			this.add(this.form);

			// Slider //
			this.form.add(new RadioChoice<RecordStatus>("status", Arrays
					.asList(RecordStatus.pending, RecordStatus.taken,
							RecordStatus.completed, RecordStatus.debt,
							RecordStatus.canceled)).setRequired(true));

			// FeedbackPanel //
			this.feedback = new JQueryFeedbackPanel("feedback");
			this.form.add(this.feedback);
		}

		@Override
		protected IModel<?> initModel() {
			return new Model<Userprofile>();
		}

		// AbstractFormDialog //

		@Override
		protected List<DialogButton> getButtons() {
			return Arrays.asList(this.btnSubmit, this.btnCancel);
		}

		@Override
		protected DialogButton getSubmitButton() {
			return this.btnSubmit;
		}

		@Override
		public Form<?> getForm() {
			return this.form;
		}

		// Events //

		@Override
		protected void onOpen(AjaxRequestTarget target) {
			target.add(this.form);
		}

		@Override
		public void onError(AjaxRequestTarget target) {
			target.add(this.feedback);
		}
	}

	@Override
	protected IModel<String> getPageTitle() {
		return new ResourceModel("p.home.title");
	}

}
