package by.stark.sample.webapp.page;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.util.file.Files;

import by.stark.sample.datamodel.Ebook;
import by.stark.sample.services.BookService;
import by.stark.sample.services.EbookService;

public class TestPage extends WebPage {
	@Inject
	private BookService bookService;
	@Inject
	private EbookService ebookService;

	@Override
	protected void onInitialize() {
		super.onInitialize();

		Form form = new Form("form");

		add(form);

		FileUploadField fileUploadField;

		List<FileUpload> fileUpload = new ArrayList<FileUpload>();

		form.add(fileUploadField = new FileUploadField("fileUpload"));

		AjaxButton save = new AjaxButton("submit") {

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> unused) {
				Ebook ebook = new Ebook();

				final List<FileUpload> uploads = fileUploadField
						.getFileUploads();
				if (uploads != null) {
					for (FileUpload upload : uploads) {
						// Create a new file
						File newFile = new File(ebookService.getRootFolder(),
								upload.getClientFileName());

						// Check new file, delete if it already existed
						if (newFile.exists()) {
							Files.remove(newFile);
						}
						try {
							// Save to new file
							newFile.createNewFile();
							upload.writeTo(newFile);
							ebook.setBook(bookService.get((long) 17));
							ebook.setName(upload.getClientFileName());
							ebookService.saveOrUpdate(ebook);
						} catch (Exception e) {
							throw new IllegalStateException(
									"Unable to write file", e);
						}
					}
				}
			}
		};
		form.add(save);
	}
}