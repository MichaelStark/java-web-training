package by.stark.sample.dataaccess;

import java.util.List;

import by.stark.sample.datamodel.Book;
import by.stark.sample.datamodel.Libriary;

public interface LibriaryDao extends AbstractDao<Long, Libriary> {

	List<Libriary> getAllLibriarysByBook(Book book);

	List<Libriary> getAllAvailableLibriarys4HandsByBook(Book book);

	List<Libriary> getAllAvailableLibriarys4RoomByBook(Book book);

}
