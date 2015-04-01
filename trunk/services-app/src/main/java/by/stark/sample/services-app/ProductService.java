package by.dzhivushko.sample.services;

import org.springframework.transaction.annotation.Transactional;

import by.dzhvisuhko.sample.datamodel.Product;

public interface ProductService {

	Product get(Long id);

	@Transactional
	void saveOrUpdate(Product product);

	@Transactional
	void delete(Product product);

	@Transactional
	void deleteAll();

}
