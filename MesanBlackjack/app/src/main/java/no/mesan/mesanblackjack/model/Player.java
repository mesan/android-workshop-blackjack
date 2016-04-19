package no.mesan.mesanblackjack.model;

public class Player extends Dealer {

    private int money;
    private int currentBet;
    private Hand splitHand;

    public Player(int money, Hand hand) {
        super(hand);
        this.money = money;
    }

    public void bet(int bet) {
        currentBet = bet;
        money -= bet;
    }

    public void draw() {
        money += currentBet;
    }

    public void win() {
        money += 1.5 * currentBet;
    }

    public void winBlackjack() {
        money += 2.5 * currentBet;
    }

    public int getMoney() {
        return money;
    }
}
