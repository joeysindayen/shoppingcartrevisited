package com.foo.shoppingcarttdd;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

public class Money {
	public static final Money ZERO = new Money(null, BigDecimal.ZERO);
	private Currency currency;
	private BigDecimal amount;

	public Money(Currency currency, BigDecimal amount) {
		this.currency = currency;
		this.amount = amount;
	}

	public Currency getCurrency() {
		return currency;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public Money add(Money augend) {
		if (null != currency && !currency.equals(augend.getCurrency())) {
			throw new IllegalArgumentException("Currencies must match for Money add operation.");
		}
		return new Money(augend.currency, this.amount.add(augend.getAmount()));
	}

	public Money multiply(int multiplicand) {
		return new Money(currency, this.amount.multiply(new BigDecimal(multiplicand)));
	}

	public Money multiply(BigDecimal multiplicand, RoundingMode roundingMode) {
		int scale = amount.scale();
		return new Money(currency, this.amount.multiply(multiplicand).setScale(scale, roundingMode));
	}

	public Money subtract(Money subtrahend) {
		if (null != subtrahend.getCurrency() && !currency.equals(subtrahend.getCurrency())) {
			throw new IllegalArgumentException("Currencies must match for Money subtract operation.");
		}
		return new Money(currency, this.amount.subtract(subtrahend.getAmount()));
	}
}
