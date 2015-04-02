package by.stark.sample.dataaccess.impl;

import org.springframework.stereotype.Repository;

import by.stark.sample.dataaccess.PublisherDao;
import by.stark.sample.datamodel.Publisher;

@Repository
public class PublisherDaoImpl extends AbstractDaoImpl<Long, Publisher>
		implements PublisherDao {

	protected PublisherDaoImpl() {
		super(Publisher.class);
	}
}