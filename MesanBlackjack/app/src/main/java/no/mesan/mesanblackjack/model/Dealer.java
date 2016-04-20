package no.mesan.mesanblackjack.model;

public class Dealer {

    private Hand hand;

    public Dealer(Hand hand) {
        this.hand = hand;
        hand.getCards().get(0).showCard();
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

    public void showHoleCard() {
        hand.getCards().get(hand.getCards().size()-1).showCard();
    }
}
