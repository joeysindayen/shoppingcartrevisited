package com.foo.shoppingcarttdd;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class ShoppingCart implements Item {
	private Map<String, ShoppingCartItem> items = new TreeMap<String, ShoppingCartItem>();
	private Map<String, ShoppingCartItem> processed = new TreeMap<String, ShoppingCartItem>();

	private PricingRule pricingRule;

	private String promoCode = "";
	private Money discount = Money.ZERO;

	private ShoppingCart(PricingRule pricingRule) {
		this.pricingRule = pricingRule;
	}

	public static ShoppingCart create(PricingRule pricingRule) {
		return new ShoppingCart(pricingRule);
	}

	public void add(Product product) {
		addProduct(items, product);
		processed.clear();
		discount = Money.ZERO;
	}

	public void add(Product product, String promoCode) {
		add(product);
		this.promoCode = promoCode;
	}

	public Collection<ShoppingCartItem> items() {
		applyPricingRules();
		return processed.values();
	}

	public String getPromoCode() {
		return promoCode;
	}

	@Override
	public Money total() {
		applyPricingRules();
		return itemsTotal().subtract(discount);
	}

	@Override
	public void addDiscount(Money discount) {
		this.discount = this.discount.add(discount);
	}

	ShoppingCartItem find(Product product) {
		return processed.get(product.getCode());
	}

	Money itemsTotal() {
		Money total = Money.ZERO;
		for (ShoppingCartItem item : processed.values()) {
			total = total.add(item.total());
		}
		return total;
	}

	void addFreeBundle(Product product) {
		addProduct(processed, product).addDiscount(product.getPrice());
	}

	private Item addProduct(Map<String, ShoppingCartItem> items, Product product) {
		ShoppingCartItem item = items.get(product.getCode());
		if (null == item) {
			item = new ShoppingCartItem(product);
			items.put(product.getCode(), item);
		} else {
			item.add();
		}
		return item;
	}

	private void applyPricingRules() {
		if (processed.isEmpty()) {
			for (ShoppingCartItem item : items.values()) {
				for (int i = item.getQuantity(); i > 0; i--) {
					addProduct(processed, item.getProduct());
				}
			}
			for (ShoppingCartItem item : processed.values()) {
				pricingRule.apply(item);
			}
			pricingRule.apply(this);
		}
	}
}
