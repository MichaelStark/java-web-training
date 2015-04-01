package by.dzhivushko.sample.dataaccess.impl;

import org.springframework.stereotype.Repository;

import by.dzhivushko.sample.dataaccess.ProductDao;
import by.dzhvisuhko.sample.datamodel.Product;

@Repository
public class ProductDaoImpl extends AbstractDaoImpl<Long, Product> implements ProductDao {

	protected ProductDaoImpl() {
		super(Product.class);
	}
}
