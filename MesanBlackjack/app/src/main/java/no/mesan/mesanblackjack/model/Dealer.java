package no.mesan.mesanblackjack.model;

public class Dealer extends Participant {

    public Dealer(Hand hand) {
        super(hand);
    }

    public boolean shouldDrawCard() {
        return getHand().getScore() <= 16;
    }
}
