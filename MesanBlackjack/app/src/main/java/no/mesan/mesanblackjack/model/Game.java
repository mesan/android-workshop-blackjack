package no.mesan.mesanblackjack.model;

public class Game {

    private Dealer dealer;
    private Player player;
    private Deck deck;
    private int startMoney = 100; //Configurable?

    public enum Outcome {
        PLAYER, DEALER, DRAW
    }

    public Game() {
        deck = new Deck();
        dealer = new Dealer(deck.createStartHand());
        player = new Player(deck.createStartHand(), startMoney);
    }

    public void dealAgain() {
        deck = new Deck();
        dealer.setHand(deck.createStartHand());
        player.setHand(deck.createStartHand());
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
        return player.getBalance() <= 0;
    }

    public void dealCard(Participant participant) {
        participant.dealCard(deck.getRandomCard());
    }

    public void playerSplit(Card card) {

    }

    public void playerDouble(Card card) {

    }

    public void insurance() {

    }

    public void resetPlayersHands() {
        dealer.resetHand();
        player.resetHand();
    }

    public Outcome getOutcome() {
        int playerScore = player.getHand().getScore();
        int dealerScore = dealer.getHand().getScore();

        if (playerScore == dealerScore) {
            return Outcome.DRAW;
        }

        return (playerScore > dealerScore) ? Outcome.PLAYER : Outcome.DEALER;
    }
}
