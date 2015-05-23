package by.stark.sample.webapp.page.admin.edit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import by.stark.sample.datamodel.Libriary;
import by.stark.sample.services.BookService;
import by.stark.sample.services.LibriaryService;
import by.stark.sample.webapp.page.admin.AdminPage;
import by.stark.sample.webapp.page.admin.edit.provider.LibraryDataProvider;

import com.googlecode.wicket.jquery.core.Options;
import com.googlecode.wicket.jquery.core.ajax.IJQueryAjaxAware;
import com.googlecode.wicket.jquery.core.ajax.JQueryAjaxBehavior;
import com.googlecode.wicket.jquery.ui.JQueryIcon;
import com.googlecode.wicket.jquery.ui.panel.JQueryFeedbackPanel;
import com.googlecode.wicket.jquery.ui.widget.dialog.AbstractFormDialog;
import com.googlecode.wicket.jquery.ui.widget.dialog.DialogButton;
import com.googlecode.wicket.jquery.ui.widget.dialog.DialogButtons;
import com.googlecode.wicket.jquery.ui.widget.dialog.DialogIcon;
import com.googlecode.wicket.jquery.ui.widget.dialog.MessageDialog;
import com.googlecode.wicket.kendo.ui.datatable.ColumnButton;
import com.googlecode.wicket.kendo.ui.datatable.DataTable;
import com.googlecode.wicket.kendo.ui.datatable.ToolbarAjaxBehavior;
import com.googlecode.wicket.kendo.ui.datatable.column.CommandsColumn;
import com.googlecode.wicket.kendo.ui.datatable.column.IColumn;
import com.googlecode.wicket.kendo.ui.datatable.column.PropertyColumn;

public class LibrarysEditPage extends AdminPage {
	@Inject
	private LibriaryService libraryService;
	@Inject
	private BookService bookService;

	public List<String> selectedValues = new ArrayList<String>();

	public LibrarysEditPage() {
		super(new ResourceModel("p.admin.librarysMenu").getObject());
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		final LibraryDialog dialog = new LibraryDialog("dialog",
				"Library details") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit(AjaxRequestTarget target) {
				Libriary library = this.getModelObject();

				libraryService.saveOrUpdate(library);
				setResponsePage(new LibrarysEditPage());
			}

		};
		this.add(dialog);

		final MessageDialog deleteDialog = new MessageDialog("warningDialog",
				new ResourceModel("p.admin.deleteWarning").getObject(),
				new ResourceModel("p.admin.deleteDialog").getObject(),
				DialogButtons.YES_NO, DialogIcon.WARN) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target, DialogButton button) {
				if (button != null && button.match(LBL_YES)) {
					for (String value : selectedValues) {
						libraryService.delete(libraryService.get(Long
								.parseLong(value)));
					}
					setResponsePage(new LibrarysEditPage());
				}
				selectedValues.clear();
			}
		};
		this.add(deleteDialog);

		List<IColumn> columns = newColumnList();

		Options options = new Options();
		options.set("height", 500);
		options.set("pageable", "{ pageSizes: [ 10, 50, 100 ] }");
		options.set("columnMenu", true);
		options.set("selectable", Options.asString("multiple"));
		options.set("groupable", true);
		options.set("toolbar", "[ { name: 'create', text: '"
				+ new ResourceModel("p.admin.createButton").getObject()
				+ "' }, { name: 'destroy', text: '"
				+ new ResourceModel("p.admin.deleteButton").getObject()
				+ "' } ]");

		List<Libriary> librarys = new ArrayList<Libriary>();
		for (Libriary libriary : libraryService.getAll()) {
			librarys.add(libraryService.getById(libriary.getId()));
		}

		IDataProvider<Libriary> provider = new LibraryDataProvider(librarys);
		final DataTable<Libriary> table = new DataTable<Libriary>("datatable",
				columns, provider, 10, options) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target, String button,
					List<String> values) {
				if (button.equals("delete") && !values.get(0).equals("")) {
					selectedValues.addAll(values);
					deleteDialog.open(target);
				} else if (button.equals("add") && !values.get(0).equals("")
						&& values.size() == 1) {
					Libriary library = new Libriary();
					library.setBook(libraryService.getById(
							Long.parseLong(values.get(0))).getBook());
					dialog.setTitle(target, new ResourceModel(
							"p.admin.dialog.addNewLibrary").getObject()
							+ " "
							+ library.getBook().getTitle());
					dialog.setModelObject(library);
					dialog.open(target);
				}
			}

			@Override
			public void onClick(AjaxRequestTarget target, ColumnButton button,
					String value) {
				Libriary library = libraryService
						.getById(Long.parseLong(value));

				dialog.setTitle(target, new ResourceModel(
						"p.admin.dialog.editLibrary").getObject()
						+ " "
						+ library.getBook().getTitle());
				dialog.setModelObject(library);
				dialog.open(target);
			}

			@Override
			protected JQueryAjaxBehavior newToolbarAjaxBehavior(
					IJQueryAjaxAware source) {
				return new ToolbarAjaxBehavior(source, "id");
			}
		};
		this.add(table);

	}

	private static List<IColumn> newColumnList() {
		List<IColumn> columns = new ArrayList<IColumn>();

		columns.add(new PropertyColumn(new ResourceModel("p.admin.id")
				.getObject(), "id", 50));
		columns.add(new PropertyColumn(new ResourceModel("p.home.book.title")
				.getObject(), "book.title"));
		columns.add(new PropertyColumn(new ResourceModel("p.home.book.isbn")
				.getObject(), "book.isbn"));
		columns.add(new PropertyColumn(new ResourceModel("p.admin.library.uin")
				.getObject(), "uin"));
		columns.add(new PropertyColumn(new ResourceModel(
				"p.admin.library.availability").getObject(), "availability"));
		columns.add(new PropertyColumn(new ResourceModel(
				"p.admin.library.forReadingRoom").getObject(), "readingRoom"));

		columns.add(new CommandsColumn("", 100) {

			private static final long serialVersionUID = 1L;

			@Override
			public List<ColumnButton> newButtons() {
				return Arrays.asList(new ColumnButton("edit",
						new ResourceModel("p.admin.editButton"), "id"));
			}
		});

		return columns;

	}

	@Override
	protected IModel<String> getPageTitle() {
		return new ResourceModel("p.home.adminLink");
	}

	abstract static class LibraryDialog extends AbstractFormDialog<Libriary> {
		private static final long serialVersionUID = 1L;
		protected final DialogButton btnSubmit = new DialogButton(
				new ResourceModel("p.admin.dialog.save").getObject(),
				JQueryIcon.CHECK);
		protected final DialogButton btnCancel = new DialogButton(
				new ResourceModel("p.admin.dialog.cancel").getObject(),
				JQueryIcon.CANCEL);

		private Form<?> form;
		private FeedbackPanel feedback;

		public LibraryDialog(String id, String title) {
			super(id, title, true);

			this.form = new Form<Libriary>("form",
					new CompoundPropertyModel<Libriary>(this.getModel()));
			this.add(this.form);

			// Slider //
			this.form.add(new RequiredTextField<String>("uin"));
			this.form.add(new CheckBox("availability").setRequired(true));
			this.form.add(new CheckBox("readingRoom").setRequired(true));

			// FeedbackPanel //
			this.feedback = new JQueryFeedbackPanel("feedback");
			this.form.add(this.feedback);
		}

		@Override
		protected IModel<?> initModel() {
			return new Model<Libriary>();
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
}
