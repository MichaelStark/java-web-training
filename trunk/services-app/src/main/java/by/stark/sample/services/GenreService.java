package by.stark.sample.services;

import org.springframework.transaction.annotation.Transactional;

import by.stark.sample.datamodel.Genre;

public interface GenreService {

	Genre get(Long id);

	@Transactional
	void saveOrUpdate(Genre genre);

	@Transactional
	void delete(Genre genre);

	@Transactional
	void deleteAll();
}
