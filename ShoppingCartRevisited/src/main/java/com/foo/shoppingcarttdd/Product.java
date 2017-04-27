package com.foo.shoppingcarttdd;

public class Product {
	private String code;
	private String name;
	private Money price;

	public Product(String code, String name, Money price) {
		this.code = code;
		this.name = name;
		this.price = price;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public Money getPrice() {
		return price;
	}
}
