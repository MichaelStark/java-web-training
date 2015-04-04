package by.stark.sample.dataaccess;

import java.util.List;

import by.stark.sample.datamodel.Book;
import by.stark.sample.datamodel.Ebook;

public interface EbookDao extends AbstractDao<Long, Ebook> {

	List<Ebook> getAllEbooksByBook(Book book);

}
