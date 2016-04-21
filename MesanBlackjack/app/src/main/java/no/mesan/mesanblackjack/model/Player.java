package no.mesan.mesanblackjack.model;

public class Player extends Dealer {

    private int balance;
    private int currentBet;
    private Hand splitHand;

    public Player(Hand hand, int balance) {
        super(hand);
        showAllCards();
        this.balance = balance;
    }

    public void bet(int bet) {
        currentBet = bet;
        balance -= bet;
    }

    public void draw() {
        balance += currentBet;
    }

    public void win() {
        balance += 1.5 * currentBet;
    }

    public void winBlackjack() {
        balance += 2.5 * currentBet;
    }

    public int getBalance() {
        return balance;
    }

    @Override
    public void setHand(Hand hand) {
        super.setHand(hand);
        showAllCards();
    }

    private void showAllCards() {
        for (Card card : getHand().getCards()) {
            card.showCard();
        }
    }
}
