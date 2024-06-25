package org.example;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MainTest {

	private Map<Character, Integer> unitPrices;
	private List<SpecialOffer> offers;
	private Kata kata;

	@Before
	public void setUp() {
		this.unitPrices = new HashMap<>();
		this.unitPrices.put('A', 50);
		this.unitPrices.put('B', 75);
		this.unitPrices.put('C', 25);
		this.unitPrices.put('D', 150);
		this.unitPrices.put('E', 200);

		this.offers = new ArrayList<>();
		this.offers.add(new MultiPricedOffer('B', 2, 125)); // 2 B's for £1.25
		this.offers.add(new BuyNGetOneFreeOffer('C', 3)); // Buy 3 C's, get one free
		this.offers.add(new MealDealOffer(List.of('D', 'E'), 300)); // D and E together for £3

		this.kata = new Kata();
	}

	@Test
	public void testNoItems() {
		final int totalPrice = this.kata.totalKataPrice("", this.unitPrices, this.offers);
		assertEquals(0, totalPrice);
	}

	@Test
	public void testSingleItemNoOffers() {
		final int totalPrice = this.kata.totalKataPrice("A", this.unitPrices, this.offers);
		assertEquals(50, totalPrice);
	}

	@Test
	public void testMultipleItemsNoOffers() {
		final int totalPrice = this.kata.totalKataPrice("AA", this.unitPrices, this.offers);
		assertEquals(100, totalPrice);
	}

	@Test
	public void testMultiPricedOffer() {
		final int totalPrice = this.kata.totalKataPrice("BB", this.unitPrices, this.offers);
		assertEquals(125, totalPrice);
	}

	@Test
	public void testBuyNGetOneFreeOffer() {
		final int totalPrice = this.kata.totalKataPrice("CCCC", this.unitPrices, this.offers);
		assertEquals(75, totalPrice); // Buy 3, get 1 free, so 4 C's for the price of 3
	}

	@Test
	public void testMealDealOffer() {
		final int totalPrice = this.kata.totalKataPrice("DE", this.unitPrices, this.offers);
		assertEquals(300, totalPrice); // D and E together for £3
	}

	@Test
	public void testCombinationOfOffers() {
		final int totalPrice = this.kata.totalKataPrice("ABBCCDDDEEE", this.unitPrices, this.offers);
		assertEquals(1125, totalPrice); // Apply all relevant offers and remaining items
	}

	@Test
	public void testEdgeCaseEmptyString() {
		final int totalPrice = this.kata.totalKataPrice("", this.unitPrices, this.offers);
		assertEquals(0, totalPrice);
	}

	@Test
	public void testEdgeCaseSingleCharacter() {
		final int totalPrice = this.kata.totalKataPrice("B", this.unitPrices, this.offers);
		assertEquals(75, totalPrice); // Single item without offers
	}

	@Test
	public void testEdgeCaseMealDealIncomplete() {
		final int totalPrice = this.kata.totalKataPrice("D", this.unitPrices, this.offers);
		assertEquals(150, totalPrice); // Only one part of the meal deal
	}

	@Test
	public void testEdgeCaseBuyNGetOneFreeNotEnoughItems() {
		final int totalPrice = this.kata.totalKataPrice("CC", this.unitPrices, this.offers);
		assertEquals(50, totalPrice); // Not enough items to get one free
	}

	@Test
	public void testEdgeCaseMultiPricedOfferNotEnoughItems() {
		final int totalPrice = this.kata.totalKataPrice("B", this.unitPrices, this.offers);
		assertEquals(75, totalPrice); // Not enough items for the special price
	}

	@Test
	public void testEdgeCaseExactItemsForOffers() {
		final int totalPrice = this.kata.totalKataPrice("BBCCCDDEE", this.unitPrices, this.offers);
		assertEquals(800, totalPrice); // Exact items for all offers to be applied
	}

	@Test
	public void testEdgeCaseExtraItems() {
		final int totalPrice = this.kata.totalKataPrice("AAABBBCCCCDDDDEEEE", this.unitPrices, this.offers);
		assertEquals(1625, totalPrice); // Extra items not covered by offers
	}
}
