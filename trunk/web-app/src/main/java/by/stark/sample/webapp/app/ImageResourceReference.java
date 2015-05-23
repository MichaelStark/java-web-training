package by.stark.sample.webapp.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.string.StringValue;

import by.stark.sample.services.PictureService;

public class ImageResourceReference extends ResourceReference {
	@Inject
	private PictureService pictureService;

	public ImageResourceReference() {
		// this creates a Key with scope 'ImageResourceReference.class'
		// and a name 'imagesDemo'
		super(ImageResourceReference.class, "imagesDemo");
		Injector.get().inject(this);
	}

	@Override
	public IResource getResource() {
		return new ImageResource();
	}

	private class ImageResource extends DynamicImageResource {

		@Override
		protected byte[] getImageData(Attributes attributes) {

			PageParameters parameters = attributes.getParameters();
			StringValue name = parameters.get("name");

			byte[] imageBytes = null;

			if (name.isEmpty() == false) {
				imageBytes = getImageAsBytes(name.toString());
			}
			return imageBytes;
		}

		private byte[] getImageAsBytes(String fileName) {

			File file = new File(pictureService.getRootFolder() + fileName);

			try {
				return IOUtils.toByteArray(new FileInputStream(file));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public boolean equals(Object that) {
			return that instanceof ImageResource;
		}
	}
}
