package com.foo.shoppingcarttdd;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.Before;
import org.junit.Test;

public class DealDiscountTest {
	private static final Currency AUD = Currency.getInstance("AUD");

	private Product product;
	private PricingRules pricingRules;
	private PricingRule dealDiscount;

	@Before
	public void setUp() throws Exception {
		this.product = new Product("ult_small", "Unlimited 1GB", new Money(AUD, new BigDecimal("24.90")));
		this.pricingRules = new PricingRules();
		this.dealDiscount = new DealDiscount(this.product, 3, 2);
		pricingRules.add(this.dealDiscount);
	}

	@Test
	public void testApplyDealDiscount() {
		ShoppingCart cart = ShoppingCart.create(pricingRules);
		cart.add(product);
		assertEquals(new BigDecimal("24.90"), cart.total().getAmount());
		cart.add(product);
		assertEquals(new BigDecimal("49.80"), cart.total().getAmount());
		cart.add(product);
		assertEquals(new BigDecimal("49.80"), cart.total().getAmount());
		cart.add(product);
		assertEquals(new BigDecimal("74.70"), cart.total().getAmount());
		cart.add(product);
		assertEquals(new BigDecimal("99.60"), cart.total().getAmount());
		cart.add(product);
		assertEquals(new BigDecimal("99.60"), cart.total().getAmount());
	}

	@Test
	public void testToString() {
		assertEquals("3 of 2 deal on \"Unlimited 1GB\"", dealDiscount.toString());
	}
}
