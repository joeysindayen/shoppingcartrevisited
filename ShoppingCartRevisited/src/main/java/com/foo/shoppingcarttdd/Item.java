package com.foo.shoppingcarttdd;

public interface Item {
	void addDiscount(Money money);

	Money total();
}
