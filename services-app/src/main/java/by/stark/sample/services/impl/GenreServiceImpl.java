/*
 * package by.stark.sample.services.impl;
 * 
 * import javax.annotation.PostConstruct; import javax.inject.Inject;
 * 
 * import org.slf4j.Logger; import org.slf4j.LoggerFactory; import
 * org.springframework.stereotype.Service;
 * 
 * import by.stark.sample.dataaccess.GenreDao; import
 * by.stark.sample.datamodel.Genre; import
 * by.stark.sample.services.GenreService;
 * 
 * @Service public class GenreServiceImpl implements GenreService {
 * 
 * private static final Logger LOGGER = LoggerFactory
 * .getLogger(GenreServiceImpl.class);
 * 
 * @Inject private GenreDao dao;
 * 
 * @PostConstruct private void init() { // this method will be called by Spring
 * after bean instantiation. Can be // used for any initialization process.
 * LOGGER.info("Instance of GenreService is created. Class is: {}",
 * getClass().getName()); }
 * 
 * @Override public Genre get(Long id) { Genre entity = dao.getById(id); return
 * entity; }
 * 
 * @Override public void saveOrUpdate(Genre genre) { if (genre.getId() == null)
 * { LOGGER.debug("Save new: {}", genre); dao.insert(genre); } else {
 * LOGGER.debug("Update: {}", genre); dao.update(genre); } }
 * 
 * @Override public void delete(Genre genre) { LOGGER.debug("Remove: {}",
 * genre); dao.delete(genre.getId()); }
 * 
 * @Override public void deleteAll() { LOGGER.debug("Remove all genres");
 * dao.deleteAll(); }
 * 
 * }
 */