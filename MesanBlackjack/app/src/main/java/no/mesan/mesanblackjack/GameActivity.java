package no.mesan.mesanblackjack;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import no.mesan.mesanblackjack.adapter.CardAdapter;
import no.mesan.mesanblackjack.adapter.ItemDecorator;
import no.mesan.mesanblackjack.model.Game;

public class GameActivity extends AppCompatActivity {

    private Game game;

    private static int MINIMUM_BET = 10;
    private int currentBet = MINIMUM_BET;

    private RecyclerView dealerRecyclerView;
    private CardAdapter dealerCardAdapter, playerCardAdapter;
    private LinearLayout resultLayout, gameOverLayout;

    public GameActivity() {
        game = new Game();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initGui();
        initListeners();
    }

    private void initGui() {
        dealerRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_dealer);
        setupRecyclerView(dealerRecyclerView);

        // TODO: Oppgave 1b: Initialiser knapp

        resultLayout = (LinearLayout)findViewById(R.id.layout_result);
        gameOverLayout = (LinearLayout)findViewById(R.id.layout_gameOver);
    }

    private void initListeners() {
        // TODO: Oppgave 1d

        gameOverLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewGame();
            }
        });
    }

    private void startNewGame() {
        game.resetPlayerBalance();
        game.resetPlayersHands();
        currentBet = MINIMUM_BET;

        dealerCardAdapter.notifyDataSetChanged();
        playerCardAdapter.notifyDataSetChanged();

        gameOverLayout.setVisibility(View.INVISIBLE);
    }

    private void dealCards() {
        game.dealAgain();
        game.playerBet(currentBet);

        dealerCardAdapter = new CardAdapter(game.getDealerHand());
        playerCardAdapter = new CardAdapter(game.getPlayerHand());

        dealerRecyclerView.setAdapter(dealerCardAdapter);

        if (game.playerHasBlackjack()) {
            playerBlackjack();
        }
    }

    private void newRound() {
        game.resetPlayersHands();
        currentBet = MINIMUM_BET;

        dealerCardAdapter.notifyDataSetChanged();
        playerCardAdapter.notifyDataSetChanged();

        resultLayout.setVisibility(View.INVISIBLE);
    }

    private void playerHits() {
        game.dealPlayerCard();

        playerCardAdapter.notifyDataSetChanged();

        if (game.playerHasBlackjack()) {
            playerStands();
        } else if (game.playerHasBusted()) {
            playerBusts();
        }
    }

    private void playerStands() {
        game.dealerShowHoleCard();

        dealerCardAdapter.notifyDataSetChanged();

        while (game.dealerShouldDrawCard()) {
            game.dealDealerCard();

            dealerCardAdapter.notifyDataSetChanged();

            if (game.dealerHasBusted()) {
                dealerBusts();
                return;
            }
        }

        Game.Outcome outcome = game.getOutcome();

        switch (outcome) {
            case DEALER:
                playerLoses();
                break;
            case PLAYER:
                playerWins();
                break;
            default:
                draw();
        }
    }

    private void playerBlackjack() {
        game.playerWinBlackjack();
        showResult();
    }

    private void playerWins() {
        game.playerWin();
        showResult();
    }

    private void dealerBusts() {
        game.playerWin();
        showResult();
    }

    private void draw() {
        game.draw();
        showResult();
    }

    private void playerBusts() {
        showResult();
    }

    private void playerLoses() {
        showResult();
    }

    private void showResult() {
        resultLayout.setVisibility(View.VISIBLE);
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(llm);
        int overlap;
        if (getResources().getDisplayMetrics().density < 3) {
            overlap = -100;
        } else {
            overlap = -150;
        }
        recyclerView.addItemDecoration(new ItemDecorator(overlap));
    }
}
