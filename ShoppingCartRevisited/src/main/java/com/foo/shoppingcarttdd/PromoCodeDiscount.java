package com.foo.shoppingcarttdd;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PromoCodeDiscount implements PricingRule {
	private String promoCode;
	private BigDecimal percentDiscount;

	public PromoCodeDiscount(String promoCode, BigDecimal percentDiscount) {
		super();
		this.promoCode = promoCode;
		this.percentDiscount = percentDiscount;
	}

	@Override
	public void apply(Item item) {
		if (item instanceof ShoppingCart) {
			ShoppingCart cart = (ShoppingCart) item;
			if (cart.getPromoCode().matches(promoCode)) {
				cart.addDiscount(cart.itemsTotal().multiply(percentDiscount, RoundingMode.HALF_EVEN));
			}
		}
	}

	@Override
	public String toString() {
		return String.format("%.0f%% discount across the board for promo code \"%s\"",
				percentDiscount.multiply(new BigDecimal("100")), promoCode);
	}
}
