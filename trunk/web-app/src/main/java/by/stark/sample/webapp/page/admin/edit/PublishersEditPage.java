package by.stark.sample.webapp.page.admin.edit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import by.stark.sample.datamodel.Publisher;
import by.stark.sample.services.PublisherService;
import by.stark.sample.webapp.page.admin.AdminPage;
import by.stark.sample.webapp.page.admin.edit.provider.PublisherDataProvider;

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

public class PublishersEditPage extends AdminPage {
	@Inject
	private PublisherService publisherService;

	public List<String> selectedValues = new ArrayList<String>();

	public PublishersEditPage() {
		super(new ResourceModel("p.admin.publishersMenu").getObject());
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		final PublisherDialog dialog = new PublisherDialog("dialog",
				"Publisher details") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit(AjaxRequestTarget target) {
				Publisher publisher = this.getModelObject();

				publisherService.saveOrUpdate(publisher);
				setResponsePage(new PublishersEditPage());
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
						publisherService.delete(publisherService.get(Long
								.parseLong(value)));
					}
					setResponsePage(new PublishersEditPage());
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

		IDataProvider<Publisher> provider = new PublisherDataProvider(
				publisherService.getAll());
		final DataTable<Publisher> table = new DataTable<Publisher>(
				"datatable", columns, provider, 10, options) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target, String button,
					List<String> values) {
				if (button.equals("delete") && !values.get(0).equals("")) {
					selectedValues.addAll(values);
					deleteDialog.open(target);
				} else if (button.equals("add")) {
					dialog.setTitle(target, new ResourceModel(
							"p.admin.dialog.addNewPublisher").getObject());
					dialog.setModelObject(new Publisher());
					dialog.open(target);
				}
			}

			@Override
			public void onClick(AjaxRequestTarget target, ColumnButton button,
					String value) {
				Publisher publisher = publisherService.get(Long
						.parseLong(value));

				dialog.setTitle(target, new ResourceModel(
						"p.admin.dialog.editPublisher").getObject()
						+ " "
						+ publisher.getName());
				dialog.setModelObject(publisher);
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
		columns.add(new PropertyColumn(new ResourceModel("p.admin.name")
				.getObject(), "name"));

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

	abstract static class PublisherDialog extends AbstractFormDialog<Publisher> {
		private static final long serialVersionUID = 1L;
		protected final DialogButton btnSubmit = new DialogButton(
				new ResourceModel("p.admin.dialog.save").getObject(),
				JQueryIcon.CHECK);
		protected final DialogButton btnCancel = new DialogButton(
				new ResourceModel("p.admin.dialog.cancel").getObject(),
				JQueryIcon.CANCEL);

		private Form<?> form;
		private FeedbackPanel feedback;

		public PublisherDialog(String id, String title) {
			super(id, title, true);

			this.form = new Form<Publisher>("form",
					new CompoundPropertyModel<Publisher>(this.getModel()));
			this.add(this.form);

			// Slider //
			this.form.add(new RequiredTextField<String>("name"));

			// FeedbackPanel //
			this.feedback = new JQueryFeedbackPanel("feedback");
			this.form.add(this.feedback);
		}

		@Override
		protected IModel<?> initModel() {
			return new Model<Publisher>();
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
