package by.stark.sample.webapp.page.admin.edit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import by.stark.sample.datamodel.Userprofile;
import by.stark.sample.datamodel.enums.UserRole;
import by.stark.sample.datamodel.enums.UserStatus;
import by.stark.sample.services.UserService;
import by.stark.sample.webapp.page.admin.AdminPage;
import by.stark.sample.webapp.page.admin.edit.provider.UserDataProvider;

import com.googlecode.wicket.jquery.core.Options;
import com.googlecode.wicket.jquery.core.ajax.IJQueryAjaxAware;
import com.googlecode.wicket.jquery.core.ajax.JQueryAjaxBehavior;
import com.googlecode.wicket.jquery.ui.JQueryIcon;
import com.googlecode.wicket.jquery.ui.form.RadioChoice;
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

public class UsersEditPage extends AdminPage {
	@Inject
	private UserService userService;

	public List<String> selectedValues = new ArrayList<String>();

	public UsersEditPage() {
		super(new ResourceModel("p.admin.usersMenu").getObject());
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		final UserDialog dialog = new UserDialog("dialog", "User details") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit(AjaxRequestTarget target) {
				Userprofile user = this.getModelObject();

				userService.saveOrUpdate(user);
				setResponsePage(new UsersEditPage());
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
						userService.delete(userService.get(Long
								.parseLong(value)));
					}
					setResponsePage(new UsersEditPage());
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
		options.set("toolbar", "[ { name: 'destroy', text: '"
				+ new ResourceModel("p.admin.deleteButton").getObject()
				+ "' } ]");

		IDataProvider<Userprofile> provider = new UserDataProvider(
				userService.getAll());
		final DataTable<Userprofile> table = new DataTable<Userprofile>(
				"datatable", columns, provider, 10, options) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target, String button,
					List<String> values) {
				if (button.equals("delete") && !values.get(0).equals("")) {
					selectedValues.addAll(values);
					deleteDialog.open(target);
				} else if (button.equals("add")) {
					// nope
				}
			}

			@Override
			public void onClick(AjaxRequestTarget target, ColumnButton button,
					String value) {
				Userprofile user = userService.get(Long.parseLong(value));

				dialog.setTitle(
						target,
						new ResourceModel("p.admin.dialog.editUser")
								.getObject() + " " + user.getEmail());
				dialog.setModelObject(user);
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
		columns.add(new PropertyColumn(new ResourceModel("p.admin.users.role")
				.getObject(), "role"));
		columns.add(new PropertyColumn(new ResourceModel("p.admin.users.email")
				.getObject(), "email"));
		columns.add(new PropertyColumn(new ResourceModel("p.admin.firstName")
				.getObject(), "firstName"));
		columns.add(new PropertyColumn(new ResourceModel("p.admin.lastName")
				.getObject(), "lastName"));
		columns.add(new PropertyColumn(
				new ResourceModel("p.admin.users.gender").getObject(), "gender"));
		columns.add(new PropertyColumn(new ResourceModel(
				"p.admin.users.birtday").getObject(), "birthday"));
		columns.add(new PropertyColumn(
				new ResourceModel("p.admin.users.status").getObject(), "status"));

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

	abstract static class UserDialog extends AbstractFormDialog<Userprofile> {
		private static final long serialVersionUID = 1L;
		protected final DialogButton btnSubmit = new DialogButton(
				new ResourceModel("p.admin.dialog.save").getObject(),
				JQueryIcon.CHECK);
		protected final DialogButton btnCancel = new DialogButton(
				new ResourceModel("p.admin.dialog.cancel").getObject(),
				JQueryIcon.CANCEL);

		private Form<?> form;
		private FeedbackPanel feedback;

		public UserDialog(String id, String title) {
			super(id, title, true);

			this.form = new Form<Userprofile>("form",
					new CompoundPropertyModel<Userprofile>(this.getModel()));
			this.add(this.form);

			// Slider //
			this.form.add(new RadioChoice<UserRole>("role", Arrays.asList(
					UserRole.admin, UserRole.librarian, UserRole.reader))
					.setRequired(true));
			this.form.add(new RadioChoice<UserStatus>("status", Arrays.asList(
					UserStatus.active, UserStatus.inactive, UserStatus.locked))
					.setRequired(true));

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
}
