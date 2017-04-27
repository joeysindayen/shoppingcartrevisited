package com.foo.shoppingcarttdd;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.Before;
import org.junit.Test;

public class PromoCodeDiscountTest {
	private static final Currency AUD = Currency.getInstance("AUD");

	private Product product;
	private PricingRules pricingRule;
	private PricingRule promoCodeDiscount;

	@Before
	public void setUp() throws Exception {
		this.product = new Product("ult_small", "Unlimited 1GB", new Money(AUD, new BigDecimal("24.90")));
		this.pricingRule = new PricingRules();
		this.promoCodeDiscount = new PromoCodeDiscount("I<3AMAYSIM", new BigDecimal(".1"));
		pricingRule.add(this.promoCodeDiscount);
	}

	@Test
	public void testApplyPromoCodeDiscount() {
		ShoppingCart cart = ShoppingCart.create(pricingRule);
		cart.add(product);
		assertEquals(new BigDecimal("24.90"), cart.total().getAmount());
		cart.add(product, "AMAYSIMPROMO");
		assertEquals(new BigDecimal("49.80"), cart.total().getAmount());
		cart.add(product, "I<3AMAYSIM");
		assertEquals(new BigDecimal("67.23"), cart.total().getAmount());
	}

	@Test
	public void testToString() {
		assertEquals("10% discount across the board for promo code \"I<3AMAYSIM\"", promoCodeDiscount.toString());
	}
}
