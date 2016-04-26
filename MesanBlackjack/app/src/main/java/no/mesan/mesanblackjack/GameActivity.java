package no.mesan.mesanblackjack;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
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
    private Button hitButton, dealButton;
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
        // TODO: Oppgave 2a: Initialiser
        setupRecyclerView(dealerRecyclerView);
        // TODO: Oppgave 2a: Setup spillerens recyclerview

        hitButton = (Button)findViewById(R.id.btn_hit);
        dealButton = (Button)findViewById(R.id.btn_deal);
        // TODO: Oppgave 2b: Initialiser knapp

        // TODO: Oppgave 2c: Initialiser views
        // TODO: Oppgave 2d: Initialiser views
        // TODO: Oppgave 2e: Initialiser views

        resultLayout = (LinearLayout)findViewById(R.id.layout_result);
        gameOverLayout = (LinearLayout) findViewById(R.id.layout_gameOver);
    }

    private void initListeners() {
        hitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerHits();
            }
        });

        // TODO: Oppgave 2b: Sett klikk-lytter

        dealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dealCards();
            }
        });

        // TODO: Oppgave 2e: Sett klikk-lytter

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

        enableDealButtons(true);

        gameOverLayout.setVisibility(View.INVISIBLE);
    }

    private void dealCards() {
        game.dealAgain();
        game.playerBet(currentBet);

        dealerCardAdapter = new CardAdapter(game.getDealerHand());
        playerCardAdapter = new CardAdapter(game.getPlayerHand());

        dealerRecyclerView.setAdapter(dealerCardAdapter);
        // TODO: Oppgave 2a: Sett adapter

        // TODO: Oppgave 2c: Oppdater spillerens score
        // TODO: Oppgave 2d: Skjul dealerens score

        enableActionButtons(true);
        enableDealButtons(false);

        if (game.playerHasBlackjack()) {
            enableActionButtons(false);
            playerBlackjack();
        }
    }

    private void newRound() {
        game.resetPlayersHands();
        currentBet = MINIMUM_BET;

        dealerCardAdapter.notifyDataSetChanged();
        playerCardAdapter.notifyDataSetChanged();

        // TODO: Oppgave 2c: Reset/skjul spillerens score
        // TODO: Oppgave 2d: Reset/skjul dealerens score

        enableActionButtons(false);
        enableDealButtons(true);

        resultLayout.setVisibility(View.INVISIBLE);
    }

    private void playerHits() {
        game.dealPlayerCard();

        playerCardAdapter.notifyDataSetChanged();
        // TODO: Oppgave 2c: Oppdater spillerens score

        if (game.playerHasBlackjack()) {
            playerStands();
            enableActionButtons(false);
        } else if (game.playerHasBusted()) {
            playerBusts();
            enableActionButtons(false);
        }
    }

    private void playerStands() {
        game.dealerShowHoleCard();

        dealerCardAdapter.notifyDataSetChanged();
        // TODO: Oppgave 2d: Vis dealerens score

        while (game.dealerShouldDrawCard()) {
            game.dealDealerCard();

            dealerCardAdapter.notifyDataSetChanged();
            // TODO: Oppgave 2d: Oppdater dealerens score

            if (game.dealerHasBusted()) {
                dealerBusts();
                enableActionButtons(false);
                return;
            }
        }

        Game.Outcome outcome = game.getOutcome();
        enableActionButtons(false);

        switch (outcome) {
            case DEALER:
                playerLoses();
                enableActionButtons(false);
                break;
            case PLAYER:
                playerWins();
                enableActionButtons(false);
                break;
            default:
                draw();
                enableActionButtons(false);
        }
    }

    private void playerBlackjack() {
        game.playerWinBlackjack();
        // TODO: Oppgave 2e: Oppdater resultat
        showResult();
    }

    private void playerWins() {
        game.playerWin();
        // TODO: Oppgave 2e: Oppdater resultat
        showResult();
    }

    private void dealerBusts() {
        game.playerWin();
        // TODO: Oppgave 2e: Oppdater resultat
        showResult();
    }

    private void draw() {
        game.draw();
        // TODO: Oppgave 2e: Oppdater resultat
        showResult();
    }

    private void playerBusts() {
        // TODO: Oppgave 2e: Oppdater resultat
        showResult();
    }

    private void playerLoses() {
        // TODO: Oppgave 2e: Oppdater resultat
        showResult();
    }

    private void showResult() {
        resultLayout.setVisibility(View.VISIBLE);
    }

    private void enableDealButtons(boolean enabled) {
        dealButton.setEnabled(enabled);
    }

    private void enableActionButtons(boolean enable) {
        hitButton.setEnabled(enable);
        // TODO: Oppgave 2b: Enable stand button
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
