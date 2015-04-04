/*package by.stark.sample.services.impl;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import by.stark.sample.dataaccess.AbstractDao;
import by.stark.sample.datamodel.AbstractEntity;
import by.stark.sample.services.AbstractService;

public abstract class AbstractServiceImpl<ID, Entity> implements
		AbstractService<ID, Entity> {

	public static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractServiceImpl.class);

	@Inject
	private AbstractDao<ID, Entity> dao;

	@Override
	public Entity get(ID id) {
		Entity entity = dao.getById(id);
		return entity;
	}

	@Override
	public void saveOrUpdate(Entity entity) {
		if (((AbstractEntity) entity).getId() == null) {
			LOGGER.debug("Save new: {}", entity);
			dao.insert(entity);
		} else {
			LOGGER.debug("Update: {}", entity);
			dao.update(entity);
		}
	}

	@Override
	public void delete(Entity entity) {
		LOGGER.debug("Remove: {}", entity);
		dao.delete((ID) ((AbstractEntity) entity).getId());
	}

	@Override
	public void deleteAll() {
		LOGGER.debug("Remove all");
		dao.deleteAll();
	}
}*/