package com.foo.shoppingcarttdd;

public class ShoppingCartItem implements Item {
	private Product product;
	private int quantity;
	private Money discount;

	public ShoppingCartItem(Product product) {
		this.product = product;
		this.quantity = 1;
		this.discount = Money.ZERO;
	}

	public Product getProduct() {
		return product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void add() {
		quantity++;
	}

	@Override
	public Money total() {
		return product.getPrice().multiply(quantity).subtract(discount);
	}

	@Override
	public void addDiscount(Money discount) {
		this.discount = this.discount.add(discount);
	}
}
