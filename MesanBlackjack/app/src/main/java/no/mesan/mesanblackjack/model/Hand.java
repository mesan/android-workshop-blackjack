package no.mesan.mesanblackjack.model;

import java.util.Collection;
import java.util.List;

public class Hand {

    private List<Card> cards;

    public Hand(List<Card> cards) {
        this.cards = cards;
    }

    private int calculateSum() {
        int sum = 0;
        for (Card card : cards) {
            if (card.getValue().getValue() == 1) {
                // TODO: 1 eller 11
            }
            sum += card.getValue().getValue();
        }
        return sum;
    }

    public List<Card> getCards() {
        return cards;
    }

    public boolean isBlackjackHand() {
        return calculateSum() == 21;
    }

    public boolean isBusted() {
        return calculateSum() > 21;
    }

    public void addCard(Card card) {
        this.cards.add(card);
    }
}
