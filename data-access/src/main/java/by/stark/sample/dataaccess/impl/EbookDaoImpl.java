package by.stark.sample.dataaccess.impl;

import org.springframework.stereotype.Repository;

import by.stark.sample.dataaccess.EbookDao;
import by.stark.sample.datamodel.Ebook;

@Repository
public class EbookDaoImpl extends AbstractDaoImpl<Long, Ebook> implements
		EbookDao {

	protected EbookDaoImpl() {
		super(Ebook.class);
	}
}