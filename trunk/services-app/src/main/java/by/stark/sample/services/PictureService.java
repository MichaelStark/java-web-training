package by.stark.sample.services;

import java.util.List;

import by.stark.sample.datamodel.Picture;

public interface PictureService extends AbstractService<Long, Picture> {

	List<Picture> getAll();

}
