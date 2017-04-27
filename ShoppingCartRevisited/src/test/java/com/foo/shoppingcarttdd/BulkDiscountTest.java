package com.foo.shoppingcarttdd;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.Before;
import org.junit.Test;

public class BulkDiscountTest {
	private static final Currency AUD = Currency.getInstance("AUD");

	private Product product;
	private PricingRules pricingRules;
	private PricingRule bulkDiscount;

	@Before
	public void setUp() throws Exception {
		this.product = new Product("ult_large", "Unlimited 5GB", new Money(AUD, new BigDecimal("44.90")));
		this.pricingRules = new PricingRules();
		this.bulkDiscount = new BulkDiscount(product, 3, new Money(AUD, new BigDecimal("39.90")));
		pricingRules.add(bulkDiscount);
	}

	@Test
	public void testApplyBulkDiscount() {
		ShoppingCart cart = ShoppingCart.create(pricingRules);
		cart.add(product);
		assertEquals(new BigDecimal("44.90"), cart.total().getAmount());
		cart.add(product);
		assertEquals(new BigDecimal("89.80"), cart.total().getAmount());
		cart.add(product);
		assertEquals(new BigDecimal("134.70"), cart.total().getAmount());
		cart.add(product);
		assertEquals(new BigDecimal("159.60"), cart.total().getAmount());
	}

	@Test
	public void testToString() {
		assertEquals("Drop to AUD 39.90 on more than 3 on \"Unlimited 5GB\"", bulkDiscount.toString());
	}
}
