package com.foo.shoppingcarttdd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

public class ShoppingCartItemTest {
	private static final Locale AUSTRALIA = new Locale("en", "AU");
	private static final Currency AUD = Currency.getInstance(AUSTRALIA);

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void whenConstructingWithProductThenItemShouldHaveTheSameProduct() {
		Product product = new Product("Code", "Name", Money.ZERO);
		ShoppingCartItem item = new ShoppingCartItem(product);
		assertEquals(1, item.getQuantity());
		assertTrue(product == item.getProduct());
	}

	@Test
	public void whenAddingQuantityThenTotalShouldEvaluateQuanityTimesPrice() {
		Product product = new Product("Code", "Name", new Money(AUD, new BigDecimal("5.20")));
		ShoppingCartItem item = new ShoppingCartItem(product);
		assertEquals(new BigDecimal("5.20"), item.total().getAmount());
		item.add();
		assertEquals(new BigDecimal("10.40"), item.total().getAmount());
		item.add();
		assertEquals(new BigDecimal("15.60"), item.total().getAmount());
	}

	@Test
	public void whenAddingDiscountThenTotalShouldTotalPriceWithDiscount() {
		Product product = new Product("Code", "Name", new Money(AUD, new BigDecimal("5.20")));
		ShoppingCartItem item = new ShoppingCartItem(product);
		item.addDiscount(new Money(AUD, new BigDecimal(".20")));
		assertEquals(new BigDecimal("5.00"), item.total().getAmount());
		item.addDiscount(new Money(AUD, new BigDecimal(".30")));
		assertEquals(new BigDecimal("4.70"), item.total().getAmount());
	}
}
