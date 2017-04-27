package com.foo.shoppingcarttdd;

public class BulkDiscount implements PricingRule {
	private Product product;
	private int moreThanQuantity;
	private Money priceDrop;
	private Money discount;

	public BulkDiscount(Product product, int moreThanQuantity, Money priceDrop) {
		super();
		this.product = product;
		this.moreThanQuantity = moreThanQuantity;
		this.priceDrop = priceDrop;
		this.discount = product.getPrice().subtract(priceDrop);
	}

	@Override
	public void apply(Item item) {
		if (item instanceof ShoppingCartItem) {
			ShoppingCartItem cartItem = (ShoppingCartItem) item;
			int itemQuantity = cartItem.getQuantity();
			if (cartItem.getProduct().getCode().matches(product.getCode()) && moreThanQuantity < itemQuantity) {
				cartItem.addDiscount(discount.multiply(itemQuantity));
			}
		}
	}

	@Override
	public String toString() {
		return String.format("Drop to %s %s on more than %d on \"%s\"", priceDrop.getCurrency(), priceDrop.getAmount(), moreThanQuantity, product.getName());
	}
}
