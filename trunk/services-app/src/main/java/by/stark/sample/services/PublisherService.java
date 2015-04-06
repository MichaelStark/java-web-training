package by.stark.sample.services;

import java.util.List;

import by.stark.sample.datamodel.Publisher;

public interface PublisherService extends AbstractService<Long, Publisher> {

	List<Publisher> getAll();

}
