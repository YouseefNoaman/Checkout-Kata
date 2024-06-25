package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
	public static void main(final String[] args) {
		System.out.println("I am working!!!");

		// Define items and their unit prices
		final HashMap<Character, Integer> unitPrices = new HashMap<>();
		unitPrices.put('A', 50);
		unitPrices.put('B', 75);
		unitPrices.put('C', 25);
		unitPrices.put('D', 150);
		unitPrices.put('E', 200);

		// Define special offers
		final List<SpecialOffer> offers = new ArrayList<>();
		offers.add(new MultiPricedOffer('B', 2, 125)); // 2 B's for £1.25
		offers.add(new BuyNGetOneFreeOffer('C', 3)); // Buy 3 C's, get one free
		offers.add(new MealDealOffer(List.of('D', 'E'), 300)); // D and E together for £3

		// Example items
		final String items = "ABBCCDDDEEE";

		// Calculate total price
		final Kata kata = new Kata();
		final int totalPrice = kata.totalKataPrice(items, unitPrices, offers);
		System.out.println("Total price: " + totalPrice + " pence");
	}
}

class Kata {

	public int totalKataPrice(final String items, final Map<Character, Integer> unitPrices, final List<SpecialOffer> offers) {
		if (items == null || items.isEmpty())
			return 0;

		final Map<Character, Integer> itemsOccurrence = this.countOccurrencesOfItems(items);

		int total = 0;

		// Apply special offers first
		for (final SpecialOffer offer : offers) {
			total += offer.apply(itemsOccurrence, unitPrices);
		}

		// Add remaining items at their unit prices
		for (final Map.Entry<Character, Integer> entry : itemsOccurrence.entrySet()) {
			total += entry.getValue() * unitPrices.get(entry.getKey());
		}

		return total;
	}

	private Map<Character, Integer> countOccurrencesOfItems(final String items) {
		final Map<Character, Integer> itemsOccurrence = new HashMap<>();

		for (char c : items.toCharArray()) {
			c = Character.toUpperCase(c);
			itemsOccurrence.put(c, itemsOccurrence.getOrDefault(c, 0) + 1);
		}
		return itemsOccurrence;
	}
}

interface SpecialOffer {
	int apply(Map<Character, Integer> itemsOccurrence, Map<Character, Integer> unitPrices);
}

class MultiPricedOffer implements SpecialOffer {
	private final char item;
	private final int quantity;
	private final int specialPrice;

	MultiPricedOffer(final char item, final int quantity, final int specialPrice) {
		this.item = item;
		this.quantity = quantity;
		this.specialPrice = specialPrice;
	}

	@Override
	public int apply(final Map<Character, Integer> itemsOccurrence, final Map<Character, Integer> unitPrices) {
		if (!itemsOccurrence.containsKey(this.item)) {
			return 0;
		}
		final int itemCount = itemsOccurrence.get(this.item);
		final int offerBundles = itemCount / this.quantity;
		final int remainingItems = itemCount % this.quantity;
		final int totalPrice = offerBundles * this.specialPrice + remainingItems * unitPrices.get(this.item);
		itemsOccurrence.put(this.item, 0); // Clear the count for this item as it's been processed
		return totalPrice;
	}
}

class BuyNGetOneFreeOffer implements SpecialOffer {
	private final char item;
	private final int nToGetFreeItem;

	BuyNGetOneFreeOffer(final char item, final int nToGetFreeItem) {
		this.item = item;
		this.nToGetFreeItem = nToGetFreeItem;
	}

	@Override
	public int apply(final Map<Character, Integer> itemsOccurrence, final Map<Character, Integer> unitPrices) {
		if (!itemsOccurrence.containsKey(this.item)) {
			return 0;
		}
		final int itemCount = itemsOccurrence.get(this.item);
		final int fullPriceCount = itemCount - (itemCount / (this.nToGetFreeItem + 1));
		final int totalPrice = fullPriceCount * unitPrices.get(this.item);
		itemsOccurrence.put(this.item, 0); // Clear the count for this item as it's been processed
		return totalPrice;
	}
}

class MealDealOffer implements SpecialOffer {
	private final List<Character> items;
	private final int specialPrice;

	MealDealOffer(final List<Character> items, final int specialPrice) {
		this.items = items;
		this.specialPrice = specialPrice;
	}

	@Override
	public int apply(final Map<Character, Integer> itemsOccurrence, final Map<Character, Integer> unitPrices) {
		int dealCount = Integer.MAX_VALUE;
		for (final char item : this.items) {
			if (!itemsOccurrence.containsKey(item)) {
				return 0;
			}
			dealCount = Math.min(dealCount, itemsOccurrence.get(item));
		}

		if (dealCount == 0) {
			return 0;
		}

		for (final char item : this.items) {
			itemsOccurrence.put(item, itemsOccurrence.get(item) - dealCount);
		}

		return dealCount * this.specialPrice;
	}
}
