package by.dzhivushko.sample.services.impl;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import by.dzhivushko.sample.dataaccess.ProductDao;
import by.dzhivushko.sample.services.ProductService;
import by.dzhvisuhko.sample.datamodel.Product;

@Service
public class ProductServiceImpl implements ProductService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Inject
	private ProductDao dao;

	@PostConstruct
	private void init() {
		// this method will be called by Spring after bean instantiation. Can be
		// used for any initialization process.
		LOGGER.info("Instance of ProductService is created. Class is: {}", getClass().getName());
	}

	@Override
	public Product get(Long id) {
		Product entity = dao.getById(id);
		return entity;
	}

	@Override
	public void saveOrUpdate(Product product) {
		if (product.getId() == null) {
			LOGGER.debug("Save new: {}", product);
			dao.insert(product);
		} else {
			LOGGER.debug("Update: {}", product);
			dao.update(product);
		}
	}

	@Override
	public void delete(Product product) {
		LOGGER.debug("Remove: {}", product);
		dao.delete(product.getId());

	}

	@Override
	public void deleteAll() {
		LOGGER.debug("Remove all products");
		dao.deleteAll();
	}
}
