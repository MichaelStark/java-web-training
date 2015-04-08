package by.stark.sample.services;

import java.util.List;

import javax.persistence.metamodel.SingularAttribute;

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

	// custom

	List<Entity> getAll(SingularAttribute<Entity, ?>... fetchAttributes);

	Entity get(SingularAttribute<? super Entity, ?> onAttr, ID id,
			SingularAttribute<Entity, ?>... fetchAttributes);

	List<Entity> getAllByField(SingularAttribute<Entity, ?> attribute,
			Object value, SingularAttribute<Entity, ?>... fetchAttributes);

}
