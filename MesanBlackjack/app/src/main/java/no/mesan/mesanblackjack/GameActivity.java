package no.mesan.mesanblackjack;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import no.mesan.mesanblackjack.adapter.CardAdapter;
import no.mesan.mesanblackjack.adapter.ItemDecorator;
import no.mesan.mesanblackjack.model.Dealer;
import no.mesan.mesanblackjack.model.Game;
import no.mesan.mesanblackjack.model.Player;

public class GameActivity extends AppCompatActivity {

    private Game game;
    private Dealer dealer;
    private Player player;

    private int currentBet = 10;

    private RecyclerView dealerRecyclerView, playerRecyclerView;
    private CardAdapter dealerCardAdapter, playerCardAdapter;
    private TextView balanceText, dealerScoreText, playerScoreText, currentBetText, betText, resultText;
    private Button hitButton, standButton, dealButton, minusButton, plusButton;
    private LinearLayout resultLayout;

    public GameActivity() {
        game = new Game();
        dealer = game.getDealer();
        player = game.getPlayer();
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
        playerRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_player);
        setupRecyclerView(dealerRecyclerView);
        setupRecyclerView(playerRecyclerView);

        balanceText = (TextView)findViewById(R.id.txt_balance);
        dealerScoreText = (TextView)findViewById(R.id.dealer_score);
        playerScoreText = (TextView)findViewById(R.id.player_score);
        currentBetText = (TextView)findViewById(R.id.txt_currentBet);
        currentBetText.setVisibility(View.GONE);
        betText = (TextView)findViewById(R.id.txt_bet);
        betText.setText(String.valueOf(currentBet));
        resultText = (TextView)findViewById(R.id.txt_result);

        hitButton = (Button)findViewById(R.id.btn_hit);
        standButton = (Button)findViewById(R.id.btn_stand);
        dealButton = (Button)findViewById(R.id.btn_deal);
        minusButton = (Button)findViewById(R.id.btn_minus);
        minusButton.setEnabled(false);
        plusButton = (Button)findViewById(R.id.btn_plus);

        resultLayout = (LinearLayout)findViewById(R.id.layout_result);
    }

    private void initListeners() {
        dealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewGame();
            }
        });

        hitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerHits();
            }
        });

        standButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerStands();
            }
        });

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseBet();
            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseBet();
            }
        });

        resultLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });
    }

    private void decreaseBet() {
        if (currentBet > 10) {
            plusButton.setEnabled(true);
            currentBet -= 10;
            if (currentBet == 10) {
                minusButton.setEnabled(false);
            }
            betText.setText(String.valueOf(currentBet));
        }
    }

    private void increaseBet() {
        if (currentBet <= player.getBalance() - 10) {
            minusButton.setEnabled(true);
            currentBet += 10;
            if (currentBet == player.getBalance()) {
                plusButton.setEnabled(false);
            }
            betText.setText(String.valueOf(currentBet));
        }
    }

    private void startNewGame() {
        game.dealAgain();
        player.bet(currentBet);

        dealerCardAdapter = new CardAdapter(dealer.getHand());
        playerCardAdapter = new CardAdapter(player.getHand());

        dealerRecyclerView.setAdapter(dealerCardAdapter);
        playerRecyclerView.setAdapter(playerCardAdapter);

        balanceText.setText(String.valueOf(player.getBalance()));
        currentBetText.setText(String.valueOf(currentBet));
        currentBetText.setVisibility(View.VISIBLE);

        if (game.playerBlackjack()) {
            enableActionButtons(false);
            playerBlackjackResponse();
            return;
        }

        // TODO: Sjekk om to like
        // TODO: Sjekk om dealer har A som åpent kort

        enableActionButtons(true);
        enableDealButtons(false);
    }

    private void playerHits() {
        game.dealCard(player);
        playerScoreText.setText(String.valueOf(player.getHand().getScore()));
        playerCardAdapter.notifyDataSetChanged();

        if (player.hasBlackjack()) {
            enableActionButtons(false);
            playerStands();
        } else if (game.playerBust()) {
            enableActionButtons(false);
            playerBustResponse();
        }
    }

    private void playerStands() {
        dealer.showHoleCard();
        dealerCardAdapter.notifyDataSetChanged();

        while (dealer.getHand().getScore() <= 16) {
            game.dealCard(dealer);
            dealerCardAdapter.notifyDataSetChanged();

            if (dealer.hasBusted()) {
                enableActionButtons(false);
                dealerBustResponse();
                return;
            }
        }

        if (player.getHand().getScore() == dealer.getHand().getScore()) {
            enableActionButtons(false);
            drawResponse();
        } else if (player.getHand().getScore() > dealer.getHand().getScore()) {
            enableActionButtons(false);
            playerWinResponse();
        } else if (player.getHand().getScore() < dealer.getHand().getScore()) {
            enableActionButtons(false);
            playerLoseResponse();
        }
    }

    private void playerBlackjackResponse() {
        // TODO: Dealer blackjack?
        player.winBlackjack();
        resultText.setText("You got blackjack!");
        showResult();
    }

    private void playerBustResponse() {
        resultText.setText("You busted!");
        showResult();
    }

    private void dealerBustResponse() {
        player.win();
        resultText.setText("Dealer busted!");
        showResult();
    }

    private void drawResponse() {
        resultText.setText("Draw!");
        player.draw();
        resultLayout.setVisibility(View.VISIBLE);
    }

    private void playerWinResponse() {
        player.win();
        resultText.setText("You won!");
        showResult();
    }

    private void playerLoseResponse() {
        resultText.setText("Dealer won!");
        showResult();
    }

    private void showResult() {
        resultLayout.setVisibility(View.VISIBLE);
        balanceText.setText(String.valueOf(player.getBalance()));
    }

    private void resetGame() {
        // TODO: Sjekk om game over
        game.resetPlayersHands();
        currentBet = 10; // Eller beholde samme bet? Evt sette ned til lik balance
        betText.setText(String.valueOf(currentBet));

        dealerCardAdapter.notifyDataSetChanged();
        playerCardAdapter.notifyDataSetChanged();

        dealerScoreText.setText(String.valueOf(dealer.getHand().getScore()));
        playerScoreText.setText(String.valueOf(player.getHand().getScore()));
        balanceText.setText(String.valueOf(player.getBalance()));
        currentBetText.setVisibility(View.GONE);

        resultLayout.setVisibility(View.INVISIBLE);

        enableActionButtons(false);
        enableDealButtons(true);

        if (game.playerBlackjack()) {
            enableActionButtons(false);
            playerBlackjackResponse();
            return;
        }
    }

    private void enableDealButtons(boolean enabled) {
        dealButton.setEnabled(enabled);
        minusButton.setEnabled(enabled);
        plusButton.setEnabled(enabled);
    }

    private void enableActionButtons(boolean enable) {
        hitButton.setEnabled(enable);
        standButton.setEnabled(enable);
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
