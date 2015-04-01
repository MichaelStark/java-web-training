package by.dzhivushko.sample.services;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import by.dzhivushko.sample.AbstractServiceTest;
import by.dzhvisuhko.sample.datamodel.Product;

public class ProductServiceTest extends AbstractServiceTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceTest.class);

	@Inject
	private ProductService productService;

	@Before
	public void cleanUpData() {
		LOGGER.info("Instance of ProductService is injected. Class is: {}", productService.getClass().getName());
		productService.deleteAll();
	}

	@Test
	public void basicCRUDTest() {
		Product product = createProduct();
		productService.saveOrUpdate(product);

		Product productFromDb = productService.get(product.getId());
		Assert.assertNotNull(productFromDb);
		Assert.assertEquals(productFromDb.getName(), product.getName());
		// TODO check other fields

		productFromDb.setName("newName");
		productService.saveOrUpdate(productFromDb);
		Product productFromDbUpdated = productService.get(product.getId());
		Assert.assertEquals(productFromDbUpdated.getName(), productFromDb.getName());
		Assert.assertNotEquals(productFromDbUpdated.getName(), product.getName());

		productService.delete(productFromDbUpdated);
		Assert.assertNull(productService.get(product.getId()));
	}

	private Product createProduct() {
		Product product = new Product();
		product.setName(randomString("name-"));
		product.setBasePrice(randomBigDecimal());
		product.setAvailable(randomBoolean());
		return product;
	}
}
