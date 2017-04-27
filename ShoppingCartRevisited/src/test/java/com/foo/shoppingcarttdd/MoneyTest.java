package com.foo.shoppingcarttdd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

public class MoneyTest {
	private static final Locale AUSTRALIA = new Locale("en", "AU");
	private static final Currency AUD = Currency.getInstance(AUSTRALIA);

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void whenContructingMoneyWithCurrencyAndAmountThenMoneyShouldHaveTheSameCurrencyAndAmount() {
		Money money = new Money(AUD, BigDecimal.ZERO);
		assertEquals("$", money.getCurrency().getSymbol(AUSTRALIA));
		assertEquals(BigDecimal.ZERO, money.getAmount());
	}

	@Test
	public void whenAddingMoneyThenCurrencyShouldMatchAndAmountsSummed() {
		Money money = new Money(AUD, new BigDecimal("2.30")).add(new Money(AUD, new BigDecimal("3.35")));
		assertEquals(AUD, money.getCurrency());
		assertEquals(new BigDecimal("5.65"), money.getAmount());
		try {
			money.add(new Money(Currency.getInstance("USD"), new BigDecimal("100.00")));
			fail();
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void whenSubtractingMoneyThenCurrencyShouldMatchAndAmountsSubtracted() {
		Money money = new Money(AUD, new BigDecimal("2.30")).subtract(new Money(AUD, new BigDecimal(".35")));
		assertEquals(AUD, money.getCurrency());
		assertEquals(new BigDecimal("1.95"), money.getAmount());
		try {
			money.subtract(new Money(Currency.getInstance("USD"), new BigDecimal(".10")));
			fail();
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void whenMultiplyingIntegerThenMoneyShouldResultWithTheSameCurrencyAndMultipliedAmount() {
		Money money = new Money(AUD, new BigDecimal("2.30")).multiply(3);
		assertEquals(AUD, money.getCurrency());
		assertEquals(new BigDecimal("6.90"), money.getAmount());
	}

	@Test
	public void whenMultiplyingFractionThenMoneyShouldResultWithTheSameCurrencyAndMultipliedAmount() {
		Money money = new Money(AUD, new BigDecimal("2.30")).multiply(new BigDecimal(".10"), RoundingMode.HALF_EVEN);
		assertEquals(AUD, money.getCurrency());
		assertEquals(new BigDecimal(".23"), money.getAmount());
	}
}
