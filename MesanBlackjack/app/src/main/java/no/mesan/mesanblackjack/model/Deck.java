package no.mesan.mesanblackjack.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Deck {

    private LinkedList<Card> cards = new LinkedList<>();

    public Deck() {
        for (Suit suit : Suit.values()) {
            for (Value value : Value.values()) {
                cards.add(new Card(suit, value));
            }
        }
        Collections.shuffle(cards);
    }

    public Card getRandomCard() {
        return cards.pop();
    }

    public Hand createStartHand() {
        List<Card> twoCards = Arrays.asList(getRandomCard(), getRandomCard());
        return new Hand(twoCards);
    }
}
