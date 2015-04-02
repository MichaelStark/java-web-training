package by.stark.sample.services;

import org.springframework.transaction.annotation.Transactional;

import by.stark.sample.datamodel.Book;

public interface BookService {

	Book get(Long id);

	@Transactional
	void saveOrUpdate(Book book);

	@Transactional
	void delete(Book book);

	@Transactional
	void deleteAll();
}
