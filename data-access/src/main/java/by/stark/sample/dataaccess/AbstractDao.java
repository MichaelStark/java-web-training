package by.stark.sample.dataaccess;

import java.util.List;

import javax.persistence.metamodel.SingularAttribute;

public interface AbstractDao<ID, Entity> {

	Entity getById(ID id);

	List<Entity> getAll();

	void delete(ID key);

	void deleteAll();

	void delete(List<ID> ids);

	Entity insert(Entity entity);

	Entity update(Entity entity);

	// custom

	List<Entity> getAll(SingularAttribute<Entity, ?>... fetchAttributes);

	Entity getById(SingularAttribute<? super Entity, ?> onAttr, ID id,
			SingularAttribute<Entity, ?>... fetchAttributes);

	List<Entity> getAllByFieldRestriction(
			SingularAttribute<? super Entity, ?> attribute, Object value,
			SingularAttribute<Entity, ?>... fetchAttributes);

}
