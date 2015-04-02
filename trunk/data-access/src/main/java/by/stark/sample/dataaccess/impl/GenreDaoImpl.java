package by.stark.sample.dataaccess.impl;

import org.springframework.stereotype.Repository;

import by.stark.sample.dataaccess.GenreDao;
import by.stark.sample.datamodel.Genre;

@Repository
public class GenreDaoImpl extends AbstractDaoImpl<Long, Genre> implements
		GenreDao {

	protected GenreDaoImpl() {
		super(Genre.class);
	}

}
