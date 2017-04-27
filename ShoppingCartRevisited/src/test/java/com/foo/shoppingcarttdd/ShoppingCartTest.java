package com.foo.shoppingcarttdd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Currency;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShoppingCartTest {
	private Logger log = LoggerFactory.getLogger(ShoppingCartTest.class);

	private static final Locale AUSTRALIA = new Locale("en", "AU");
	private static final Currency AUD = Currency.getInstance(AUSTRALIA);

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void whenCreatingWithANullPricingRuleThenItShouldNotFail() {
		ShoppingCart cart = ShoppingCart.create(null);
		assertNotNull(cart);
	}

	@Test
	public void whenCreatingWithPricingRuleThenItShouldNotFail() {
		ShoppingCart cart = ShoppingCart.create(new PricingRules());
		assertNotNull(cart);
	}

	@Test
	public void whenAddingWithProductThenItShouldHaveAnItemOrItemsOfTheProduct() {
		ShoppingCart cart = ShoppingCart.create(new PricingRules());

		assertEquals(0, cart.items().size());

		Product product = new Product("code", "name", Money.ZERO);

		cart.add(product);
		assertEquals(1, cart.items().size());
		ShoppingCartItem item = cart.items().iterator().next();
		assertEquals(1, item.getQuantity());
		assertTrue(product == item.getProduct());

		cart.add(product);
		assertEquals(1, cart.items().size());
		item = cart.items().iterator().next();
		assertEquals(2, item.getQuantity());
	}

	@Test
	public void whenAddingWithProductAndPromoCodeThenItShouldAddAnItemsOfTheProductAndSetThePromoCode() {
		ShoppingCart cart = ShoppingCart.create(new PricingRules());
		Product product = new Product("code", "name", Money.ZERO);
		cart.add(product, "This is a sample promo code");
		assertEquals(1, cart.items().size());
		ShoppingCartItem item = cart.items().iterator().next();
		assertEquals(1, item.getQuantity());
		assertTrue(product == item.getProduct());
		assertEquals("This is a sample promo code", cart.getPromoCode());
	}

	@Test
	public void whenAddingWithPricedProductMultipleTimeThenCartShouldTotalToMultiplesOfThePrice() {
		ShoppingCart cart = ShoppingCart.create(new PricingRules());
		Product product = new Product("code", "name", new Money(AUD, new BigDecimal("2.90")));

		cart.add(product);
		assertEquals(new BigDecimal("2.90"), cart.total().getAmount());
		ShoppingCartItem item = cart.items().iterator().next();
		assertEquals(1, item.getQuantity());
		assertTrue(product == item.getProduct());

		cart.add(product);
		assertEquals(1, cart.items().size());
		item = cart.items().iterator().next();
		assertEquals(2, item.getQuantity());
		assertEquals(new BigDecimal("5.80"), cart.total().getAmount());
	}

	@Test
	public void whenAddingDiscountThenTotalShouldTotalPriceWithDiscount() {
		ShoppingCart cart = ShoppingCart.create(new PricingRules());
		Product product = new Product("code", "name", new Money(AUD, new BigDecimal("2.90")));
		cart.add(product);
		assertEquals(new BigDecimal("2.90"), cart.total().getAmount());
		cart.addDiscount(new Money(AUD, new BigDecimal(".20")));
		assertEquals(new BigDecimal("2.70"), cart.total().getAmount());
		cart.addDiscount(new Money(AUD, new BigDecimal(".30")));
		assertEquals(new BigDecimal("2.40"), cart.total().getAmount());
	}

	@Test
	public void whenTotallingThenPricingRulesShouldBeAppliedOnlyOnceToTheItemTotalButReappliedIfNewItemAdded() {
		ShoppingCart cart = ShoppingCart.create(new PricingRule() {
			@Override
			public void apply(Item item) {
				if (item instanceof ShoppingCart) {
					ShoppingCart cart = (ShoppingCart) item;
					Money itemsTotal = cart.itemsTotal();
					cart.addDiscount(itemsTotal.multiply(new BigDecimal(".10"), RoundingMode.HALF_EVEN));
				}
			}
		});

		Product product1 = new Product("code1", "name1", new Money(AUD, new BigDecimal("3.00")));
		cart.add(product1);
		assertEquals(new BigDecimal("2.70"), cart.total().getAmount());
		assertEquals(new BigDecimal("2.70"), cart.total().getAmount());

		cart.add(new Product("code2", "name2", new Money(AUD, new BigDecimal("4.00"))));
		assertEquals(new BigDecimal("6.30"), cart.total().getAmount());

		cart.add(product1);
		assertEquals(new BigDecimal("9.00"), cart.total().getAmount());
	}

	@Test
	public void whenTotallingThenItemPricingRulesShouldBeAppliedOnlyOnceToTheItemTotalButReappliedIfNewItemAdded() {
		PricingRule pricingRule = new PricingRule() {
			@Override
			public void apply(Item item) {
				if (item instanceof ShoppingCartItem) {
					ShoppingCartItem cartItem = (ShoppingCartItem) item;
					cartItem.addDiscount(new Money(AUD, new BigDecimal(".30")));
				}
			}
		};
		PricingRules pricingRules = new PricingRules();
		pricingRules.add(pricingRule);

		ShoppingCart cart = ShoppingCart.create(pricingRules);
		cart.add(new Product("code1", "name1", new Money(AUD, new BigDecimal("3.00"))));
		assertEquals(new BigDecimal("2.70"), cart.total().getAmount());
		assertEquals(new BigDecimal("2.70"), cart.total().getAmount());
		cart.add(new Product("code2", "name2", new Money(AUD, new BigDecimal("4.00"))));
		assertEquals(new BigDecimal("6.40"), cart.total().getAmount());
	}

	@Test
	public void whenPricingRuleAddsFreeBundleThenItemsShouldShowFreeBundle() {
		Product product = new Product("code1", "name1", new Money(AUD, new BigDecimal("3.00")));
		ShoppingCart cart = ShoppingCart.create(new PricingRule() {
			@Override
			public void apply(Item item) {
				if (item instanceof ShoppingCart) {
					ShoppingCart cart = (ShoppingCart) item;
					cart.addFreeBundle(product);
				}
			}
		});

		cart.add(product);
		Collection<ShoppingCartItem> items = cart.items();
		assertEquals(1, items.size());
		assertEquals(2, cart.find(product).getQuantity());
		assertEquals(new BigDecimal("3.00"), cart.total().getAmount());

		cart.add(product);
		items = cart.items();
		assertEquals(1, items.size());
		assertEquals(3, cart.find(product).getQuantity());
		assertEquals(new BigDecimal("6.00"), cart.total().getAmount());
	}
}
