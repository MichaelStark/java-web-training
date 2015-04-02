package by.stark.sample.dataaccess.impl;

import org.springframework.stereotype.Repository;

import by.stark.sample.dataaccess.LibriaryDao;
import by.stark.sample.datamodel.Libriary;

@Repository
public class LibriaryDaoImpl extends AbstractDaoImpl<Long, Libriary> implements
		LibriaryDao {

	protected LibriaryDaoImpl() {
		super(Libriary.class);
	}
}