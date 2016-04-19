package no.mesan.mesanblackjack.model;

public class Dealer {

    private Hand hand;

    public Dealer(Hand hand) {
        this.hand = hand;
    }

    public Hand getHand() {
        return hand;
    }

    public boolean hasBlackjack() {
        return hand.isBlackjackHand();
    }

    public boolean hasBusted() {
        return hand.isBusted();
    }

    public void dealCard(Card card) {
        hand.addCard(card);
    }
}
