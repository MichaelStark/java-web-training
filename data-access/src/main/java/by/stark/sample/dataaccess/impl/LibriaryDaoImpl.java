package by.stark.sample.dataaccess.impl;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import by.stark.sample.dataaccess.LibriaryDao;
import by.stark.sample.datamodel.Book;
import by.stark.sample.datamodel.Libriary;
import by.stark.sample.datamodel.Libriary_;

@Repository
public class LibriaryDaoImpl extends AbstractDaoImpl<Long, Libriary> implements
		LibriaryDao {

	protected LibriaryDaoImpl() {
		super(Libriary.class);
	}

	@Override
	public List<Libriary> getAllLibriarysByBook(Book book,
			Boolean availability, Boolean room) {
		CriteriaBuilder cBuilder = getEm().getCriteriaBuilder();

		CriteriaQuery<Libriary> root = cBuilder.createQuery(Libriary.class);
		Root<Libriary> criteria = root.from(Libriary.class);

		root.select(criteria);

		root.where(cBuilder.and(cBuilder.equal(criteria.get(Libriary_.book),
				book),
				cBuilder.equal(criteria.get(Libriary_.readingRoom), room),
				cBuilder.equal(criteria.get(Libriary_.availability),
						availability)));

		TypedQuery<Libriary> query = getEm().createQuery(root);
		List<Libriary> results = query.getResultList();
		return results;
	}
}