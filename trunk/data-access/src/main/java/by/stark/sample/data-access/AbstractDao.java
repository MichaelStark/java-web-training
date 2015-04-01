package by.dzhivushko.sample.dataaccess;

import java.util.List;

public interface AbstractDao<ID, Entity> {

	Entity getById(ID id);

	List<Entity> getAll();

	void delete(ID key);

	void deleteAll();

	void delete(List<ID> ids);

	Entity insert(Entity entity);

	Entity update(Entity entity);
}
