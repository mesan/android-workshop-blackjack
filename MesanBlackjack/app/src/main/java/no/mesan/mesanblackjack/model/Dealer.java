package no.mesan.mesanblackjack.model;

public class Dealer {

    private Hand hand;

    public Dealer(Hand hand) {
        setHand(hand);
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
        card.showCard();
    }

    public void showHoleCard() {
        int holeCardPosition = hand.getCards().size()-1;
        Card holeCard = hand.getCards().get(holeCardPosition);
        holeCard.showCard();
    }

    public void resetHand() {
        hand.reset();
    }

    public void setHand(Hand hand) {
        this.hand = hand;
        hand.getCards().get(0).showCard();
    }
}
