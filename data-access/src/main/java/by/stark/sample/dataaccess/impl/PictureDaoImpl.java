package by.stark.sample.dataaccess.impl;

import org.springframework.stereotype.Repository;

import by.stark.sample.dataaccess.PictureDao;
import by.stark.sample.datamodel.Picture;

@Repository
public class PictureDaoImpl extends AbstractDaoImpl<Long, Picture> implements
		PictureDao {

	protected PictureDaoImpl() {
		super(Picture.class);
	}
}