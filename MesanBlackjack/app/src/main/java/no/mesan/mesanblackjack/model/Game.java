package no.mesan.mesanblackjack.model;

public class Game {
    private Dealer dealer;
    private Player player;
    private Deck deck;
    private int startMoney = 100;//Configurable?

    public Game() {
        deck = new Deck();
        dealer = new Dealer(deck.createStartHand());
        player = new Player(startMoney, deck.createStartHand());
    }

    public Player getPlayer() {
        return player;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public boolean dealerBlackjack() {
        return dealer.hasBlackjack();
    }

    public boolean playerBlackjack() {
        return player.hasBlackjack();
    }

    public boolean dealerBust() {
        return dealer.hasBusted();
    }

    public boolean playerBust() {
        return player.hasBusted();
    }

    public void playerBets(int bet) {
        player.bet(bet);
    }

    public boolean gameOver() {
        return player.getMoney() <= 0;
    }

    public void dealCard(Dealer player) {
        Card newCard = deck.getRandomCard();
        newCard.showCard();
        player.dealCard(newCard);
    }

    public void playerSplit(Card card) {

    }

    public void playerDouble(Card card) {

    }

    public void insurance() {

    }

}
