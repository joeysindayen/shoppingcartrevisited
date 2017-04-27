package com.foo.shoppingcarttdd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PricingRules implements PricingRule {
	private List<PricingRule> rules = new ArrayList<PricingRule>();

	public void add(PricingRule pricingRule) {
		rules.add(pricingRule);
	}

	public Collection<PricingRule> getRules() {
		return rules;
	}

	@Override
	public void apply(Item item) {
		for (PricingRule rule : rules) {
			rule.apply(item);
		}
	}
}
