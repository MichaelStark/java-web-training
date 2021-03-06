package by.stark.sample.services;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

public interface AbstractService<ID, Entity> {

	Entity get(ID id);

	@Transactional
	void saveOrUpdate(Entity entity);

	@Transactional
	void delete(Entity entity);

	@Transactional
	void deleteAll();

	List<Entity> getAll();

}
