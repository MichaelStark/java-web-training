package by.stark.sample.services;

import java.util.List;

import by.stark.sample.datamodel.Book;
import by.stark.sample.datamodel.Libriary;

public interface LibriaryService extends AbstractService<Long, Libriary> {

	List<Libriary> getAllLibriarysByBook(Book book, Boolean availability,
			Boolean room);

}
