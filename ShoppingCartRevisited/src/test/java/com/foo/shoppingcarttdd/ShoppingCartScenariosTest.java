package com.foo.shoppingcarttdd;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShoppingCartScenariosTest {
	private Logger log = LoggerFactory.getLogger(ShoppingCartScenariosTest.class);

	private static final Locale AUSTRALIA = new Locale("en", "AU");
	private static final Currency AUD = Currency.getInstance(AUSTRALIA);

	private Map<String, Product> products = new HashMap<String, Product>();
	private PricingRules pricingRules = new PricingRules();

	@Before
	public void setUp() throws Exception {
		addProduct(new Product("ult_small", "Unlimited 1GB", new Money(AUD, new BigDecimal("24.90"))));
		addProduct(new Product("ult_medium", "Unlimited 2GB", new Money(AUD, new BigDecimal("29.9"))));
		addProduct(new Product("ult_large", "Unlimited 5GB", new Money(AUD, new BigDecimal("44.90"))));
		addProduct(new Product("1gb", "1 GB Data-pack", new Money(AUD, new BigDecimal("9.9"))));

		pricingRules.add(new DealDiscount(findProduct("ult_small"), 3, 2));
		pricingRules.add(new BulkDiscount(findProduct("ult_large"), 3, new Money(AUD, new BigDecimal("39.9"))));
		pricingRules.add(new FreeBundle(findProduct("ult_medium"), findProduct("1gb")));
		pricingRules.add(new PromoCodeDiscount("I<3AMAYSIM", new BigDecimal(".1")));
	}

	@Test
	public void testScenario1() {
		log.info("----------------------------------------");
		log.info("Scenario 1");
		log.info("----------------------------------------");
		ShoppingCart cart = ShoppingCart.create(pricingRules);
		Product product = findProduct("ult_small");
		cart.add(product);
		cart.add(product);
		cart.add(product);
		cart.add(findProduct("ult_large"));

		Collection<ShoppingCartItem> items = cart.items();
		for (Item item : items) {
			ShoppingCartItem cartItem = (ShoppingCartItem) item;
			log.info("{} x {}", cartItem.getQuantity(), cartItem.getProduct().getName());
		}
		assertEquals(2, items.size());
		assertEquals(3, cart.find(findProduct("ult_small")).getQuantity());
		assertEquals(1, cart.find(findProduct("ult_large")).getQuantity());
		Money total = cart.total();
		log.info("Cart total is {} {}", total.getCurrency().getSymbol(AUSTRALIA), total.getAmount());
		assertEquals(new BigDecimal("94.70"), total.getAmount());
	}

	@Test
	public void testScenario2() {
		log.info("----------------------------------------");
		log.info("Scenario 2");
		log.info("----------------------------------------");
		ShoppingCart cart = ShoppingCart.create(pricingRules);

		Product product = findProduct("ult_small");
		cart.add(product);
		cart.add(product);

		product = findProduct("ult_large");
		cart.add(product);
		cart.add(product);
		cart.add(product);
		cart.add(product);

		Collection<ShoppingCartItem> items = cart.items();
		for (Item item : items) {
			ShoppingCartItem cartItem = (ShoppingCartItem) item;
			log.info("{} x {}", cartItem.getQuantity(), cartItem.getProduct().getName());
		}
		assertEquals(2, items.size());
		assertEquals(2, cart.find(findProduct("ult_small")).getQuantity());
		assertEquals(4, cart.find(findProduct("ult_large")).getQuantity());
		Money total = cart.total();
		log.info("Cart total is {} {}", total.getCurrency().getSymbol(AUSTRALIA), total.getAmount());
		assertEquals(new BigDecimal("209.40"), total.getAmount());
	}

	@Test
	public void testScenario3() {
		log.info("----------------------------------------");
		log.info("Scenario 3");
		log.info("----------------------------------------");
		ShoppingCart cart = ShoppingCart.create(pricingRules);

		cart.add(findProduct("ult_small"));

		Product product = findProduct("ult_medium");
		cart.add(product);
		cart.add(product);

		Collection<ShoppingCartItem> items = cart.items();
		for (Item item : items) {
			ShoppingCartItem cartItem = (ShoppingCartItem) item;
			log.info("{} x {}", cartItem.getQuantity(), cartItem.getProduct().getName());
		}
		assertEquals(3, items.size());
		assertEquals(1, cart.find(findProduct("ult_small")).getQuantity());
		assertEquals(2, cart.find(findProduct("ult_medium")).getQuantity());
		assertEquals(2, cart.find(findProduct("1gb")).getQuantity());
		Money total = cart.total();
		log.info("Cart total is {} {}", total.getCurrency().getSymbol(AUSTRALIA), total.getAmount());
		assertEquals(new BigDecimal("84.70"), total.getAmount());
	}

	@Test
	public void testScenario4() {
		log.info("----------------------------------------");
		log.info("Scenario 4");
		log.info("----------------------------------------");
		ShoppingCart cart = ShoppingCart.create(pricingRules);
		cart.add(findProduct("ult_small"));
		cart.add(findProduct("1gb"), "I<3AMAYSIM");

		Collection<ShoppingCartItem> items = cart.items();
		for (Item item : items) {
			ShoppingCartItem cartItem = (ShoppingCartItem) item;
			log.info("{} x {}", cartItem.getQuantity(), cartItem.getProduct().getName());
		}
		assertEquals(2, items.size());
		assertEquals(1, cart.find(findProduct("ult_small")).getQuantity());
		assertEquals(1, cart.find(findProduct("1gb")).getQuantity());
		Money total = cart.total();
		log.info("Cart total is {} {}", total.getCurrency().getSymbol(AUSTRALIA), total.getAmount());
		assertEquals(new BigDecimal("31.32"), total.getAmount());
	}

	private void addProduct(Product product) {
		products.put(product.getCode(), product);
	}

	private Product findProduct(String code) {
		return products.get(code);
	}
}
