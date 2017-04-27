package com.foo.shoppingcarttdd;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ProductTest {
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void whenConstructingProductWithCodeNameAndPriceThenProductShouldHaveTheSameCodeNameAndPrice() {
		Product product = new Product("Code", "Name", Money.ZERO);
		assertEquals("Code", product.getCode());
		assertEquals("Name", product.getName());
		assertNotNull(product.getPrice());
		assertTrue(product.getPrice() instanceof Money);
		assertTrue(Money.ZERO == product.getPrice());
	}
}
