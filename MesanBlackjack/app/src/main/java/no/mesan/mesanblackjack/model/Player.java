package no.mesan.mesanblackjack.model;

public class Player extends Participant {

    private int startBalance;
    private int balance;
    private int currentBet;
    private Hand splitHand;

    public Player(Hand hand, int balance) {
        super(hand);
        this.balance = balance;
        this.startBalance = balance;
        showAllCards();
    }

    public void bet(int bet) {
        currentBet = bet;
        balance -= bet;
    }

    public void draw() {
        balance += currentBet;
    }

    public void win() {
        balance += 2 * currentBet;
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

    public boolean isGameOver() {
        return balance < 10;
    }

    public void resetBalance() {
        this.balance = startBalance;
    }
}
