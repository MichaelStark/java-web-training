package by.stark.sample.services;

import java.util.List;

import by.stark.sample.datamodel.Genre;

public interface GenreService extends AbstractService<Long, Genre> {

	List<Genre> getAll();

}
