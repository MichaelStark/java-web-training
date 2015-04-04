package by.stark.sample.dataaccess.impl;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import by.stark.sample.dataaccess.EbookDao;
import by.stark.sample.datamodel.Book;
import by.stark.sample.datamodel.Ebook;
import by.stark.sample.datamodel.Ebook_;

@Repository
public class EbookDaoImpl extends AbstractDaoImpl<Long, Ebook> implements
		EbookDao {

	protected EbookDaoImpl() {
		super(Ebook.class);
	}

	@Override
	public List<Ebook> getAllEbooksByBook(Book book) {
		CriteriaBuilder cBuilder = getEm().getCriteriaBuilder();

		CriteriaQuery<Ebook> root = cBuilder.createQuery(Ebook.class);
		Root<Ebook> criteria = root.from(Ebook.class);

		root.select(criteria);

		root.where(cBuilder.equal(criteria.get(Ebook_.book), book));

		TypedQuery<Ebook> query = getEm().createQuery(root);
		List<Ebook> results = query.getResultList();
		return results;
	}
}