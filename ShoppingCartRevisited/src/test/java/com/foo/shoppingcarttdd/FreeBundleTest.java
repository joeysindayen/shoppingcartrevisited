package com.foo.shoppingcarttdd;

import static org.junit.Assert.assertEquals;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Currency;

import org.junit.Before;
import org.junit.Test;

public class FreeBundleTest {
	private static final Currency AUD = Currency.getInstance("AUD");

	private Product product;
	private Product bundle;
	private PricingRules pricingRules;
	private PricingRule freeBundle;

	@Before
	public void setUp() throws Exception {
		this.product = new Product("ult_medium", "Unlimited 2GB", new Money(AUD, new BigDecimal("29.90")));
		this.bundle = new Product("1gb", "1 GB Data-pack", new Money(AUD, new BigDecimal("9.90")));
		this.pricingRules = new PricingRules();
		this.freeBundle = new FreeBundle(product, bundle);
		pricingRules.add(this.freeBundle);
	}

	@Test
	public void testApplyFreeBundle() {
		ShoppingCart cart = ShoppingCart.create(pricingRules);
		cart.add(product);

		Collection<ShoppingCartItem> items = cart.items();

		assertEquals(2, items.size());
		assertEquals(1, cart.find(product).getQuantity());
		assertEquals(1, cart.find(bundle).getQuantity());

		assertEquals(new BigDecimal("29.90"), cart.total().getAmount());
	}

	@Test
	public void testToString() {
		assertEquals("Free \"1 GB Data-pack\" bundled for every \"Unlimited 2GB\"", freeBundle.toString());
	}
}
