package com.foo.shoppingcarttdd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Iterator;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

public class PricingRulesTest {
	private static final Locale AUSTRALIA = new Locale("en", "AU");
	private static final Currency AUD = Currency.getInstance(AUSTRALIA);

	private PricingRules pricingRules;
	private PricingRule rule1;
	private PricingRule rule2;

	@Before
	public void setUp() throws Exception {
		pricingRules = new PricingRules();
		rule1 = new PricingRule() {
			@Override
			public void apply(Item item) {
				item.addDiscount(new Money(AUD, new BigDecimal("0.1")));
			}
		};
		rule2 = new PricingRule() {
			@Override
			public void apply(Item item) {
				item.addDiscount(new Money(AUD, new BigDecimal("0.5")));
			}
		};
		pricingRules.add(rule1);
		pricingRules.add(rule2);
	}

	@Test
	public void whenAddingAPricingRuleThenPricingRulesShouldBeAbleToComposeInOrder() {
		assertEquals(2, pricingRules.getRules().size());

		Iterator<PricingRule> rulesIterator = pricingRules.getRules().iterator();
		assertTrue(rulesIterator.next() == rule1);
		assertTrue(rulesIterator.next() == rule2);

		Item item = new Item() {
			Money price = new Money(AUD, new BigDecimal("20.5"));
			Money discount = Money.ZERO;
			
			@Override
			public Money total() {
				return price.subtract(discount);
			}

			@Override
			public void addDiscount(Money discount) {
				this.discount = this.discount.add(discount);
			}
		};
		pricingRules.apply(item);
		assertEquals(new BigDecimal("19.9"), item.total().getAmount());
	}
}
