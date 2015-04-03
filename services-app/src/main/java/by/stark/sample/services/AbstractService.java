package by.stark.sample.services;

import org.springframework.transaction.annotation.Transactional;

public interface AbstractService<ID, Entity> {

	@Transactional
	Entity get(ID id);

	@Transactional
	void saveOrUpdate(Entity entity);

	@Transactional
	void delete(Entity entity);

	@Transactional
	void deleteAll();

}
