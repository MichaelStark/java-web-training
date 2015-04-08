package by.stark.sample.dataaccess.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import by.stark.sample.dataaccess.AbstractDao;

public abstract class AbstractDaoImpl<ID, Entity> implements
		AbstractDao<ID, Entity> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractDaoImpl.class);

	private EntityManager em;
	private final Class<Entity> entityClass;

	protected AbstractDaoImpl(final Class<Entity> entityClass) {
		Validate.notNull(entityClass, "entityClass could not be a null");
		this.entityClass = entityClass;
	}

	@Override
	public Entity getById(ID id) {
		return em.find(getEntityClass(), id);
	}

	@Override
	public Entity insert(final Entity entity) {
		em.persist(entity);
		return entity;
	}

	@Override
	public Entity update(Entity entity) {
		entity = em.merge(entity);
		em.flush();
		return entity;
	}

	@Override
	public void delete(final ID key) {
		em.remove(em.find(getEntityClass(), key));
	}

	@Override
	public void delete(List<ID> ids) {
		em.createQuery(
				String.format("delete from %s e where e.id in (:ids)",
						getEntityClass().getSimpleName()))
				.setParameter("ids", ids).executeUpdate();
	}

	@Override
	public void deleteAll() {
		final Query q1 = em.createQuery("delete from "
				+ getEntityClass().getSimpleName());
		q1.executeUpdate();
		em.flush();
	}

	@Override
	public List<Entity> getAll() {
		final CriteriaQuery<Entity> query = em.getCriteriaBuilder()
				.createQuery(getEntityClass());
		query.from(getEntityClass());
		final List<Entity> lst = em.createQuery(query).getResultList();
		return lst;
	}

	// custom

	@Override
	public List<Entity> getAll(SingularAttribute<Entity, ?>... fetchAttributes) {
		final CriteriaQuery<Entity> criteria = getEm().getCriteriaBuilder()
				.createQuery(getEntityClass());
		final Root<Entity> root = criteria.from(getEntityClass());
		criteria.select(root);
		for (SingularAttribute<Entity, ?> attribute : fetchAttributes) {
			root.fetch(attribute);
		}
		criteria.distinct(true);
		final List<Entity> lst = getEm().createQuery(criteria).getResultList();
		return lst;
	}

	@Override
	public Entity getById(final SingularAttribute<? super Entity, ?> onAttr,
			final ID id, SingularAttribute<Entity, ?>... fetchAttributes) {
		Entity result = null;
		Validate.notNull(id, "Search attributes can't be empty. Attribute: "
				+ onAttr.getName());

		final CriteriaBuilder cBuilder = getEm().getCriteriaBuilder();
		final CriteriaQuery<Entity> criteria = cBuilder
				.createQuery(getEntityClass());
		final Root<Entity> root = criteria.from(getEntityClass());

		criteria.select(root);
		for (SingularAttribute<Entity, ?> attribute : fetchAttributes) {
			root.fetch(attribute);
		}
		criteria.distinct(true);
		criteria.where(cBuilder.equal(root.get(onAttr), id));

		try {
			result = getEm().createQuery(criteria).getSingleResult();
		} catch (NoResultException e) {
			LOGGER.debug("Search result is empty: {}", e);
			return null;
		} catch (NonUniqueResultException e) {
			LOGGER.warn(
					"Search result is more than one. !RETURN FIRST RESULT! Maybe you use this method not for ID UNIQUE field: {}",
					e);
			return getEm().createQuery(criteria).getResultList().get(0);
		}
		return result;
	}

	@Override
	public List<Entity> getAllByFieldRestriction(
			final SingularAttribute<? super Entity, ?> attribute,
			final Object value, SingularAttribute<Entity, ?>... fetchAttributes) {
		Validate.notNull(value, "Search attributes can't be empty. Attribute: "
				+ attribute.getName());
		final CriteriaBuilder cBuilder = getEm().getCriteriaBuilder();
		final CriteriaQuery<Entity> criteria = cBuilder
				.createQuery(getEntityClass());
		final Root<Entity> root = criteria.from(getEntityClass());

		criteria.select(root);
		for (SingularAttribute<Entity, ?> attr : fetchAttributes) {
			root.fetch(attr);
		}
		criteria.distinct(true);
		criteria.where(cBuilder.equal(root.get(attribute), value));
		return getEm().createQuery(criteria).getResultList();
	}

	@PersistenceContext
	protected void setEntityManager(final EntityManager em) {
		LOGGER.info("Set EM {} to class {}", em.hashCode(), getClass()
				.getName());
		this.em = em;
	}

	private Class<Entity> getEntityClass() {
		return entityClass;
	}

	public EntityManager getEm() {
		return em;
	}
}
