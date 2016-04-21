package no.mesan.mesanblackjack.model;

/**
 * Created by Thomas on 21.04.2016.
 */
public abstract class Participant {

    private Hand hand;

    public Participant(Hand hand) {
        setHand(hand);
    }

    public void setHand(Hand hand) {
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

}
