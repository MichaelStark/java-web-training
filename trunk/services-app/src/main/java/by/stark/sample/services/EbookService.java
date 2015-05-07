package by.stark.sample.services;

import java.util.List;

import by.stark.sample.datamodel.Book;
import by.stark.sample.datamodel.Ebook;

public interface EbookService extends AbstractService<Long, Ebook> {

	List<Ebook> getAllByBook(Book book);

	Ebook getById(Long id);

}
