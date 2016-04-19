package no.mesan.mesanblackjack.game;

import no.mesan.mesanblackjack.model.Game;

public class BlackjackGame {

    public void gameLoop() {
        Game game = createGame();

        while(true) {
            if (game.gameOver()) {
                // TODO
                break;
            }
            //showCards();

            int bet = 100; // From user
            game.playerBets(bet);

            if (game.dealerBlackjack()) {
                // Do something
                break;
            }

            if (game.playerBlackjack()) {
                break;
            }




        }
    }

    public Game createGame() {
        return new Game();
    }
}
