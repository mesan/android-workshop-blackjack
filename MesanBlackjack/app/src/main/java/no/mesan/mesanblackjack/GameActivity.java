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
    private LinearLayout resultLayout, gameOverLayout, dealerScoreCircle, playerScoreCircle;

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
        dealerScoreCircle = (LinearLayout)findViewById(R.id.dealer_score_holder);
        dealerScoreText = (TextView)findViewById(R.id.dealer_score);
        dealerScoreCircle.setVisibility(View.GONE);
        playerScoreCircle = (LinearLayout)findViewById(R.id.player_score_holder);
        playerScoreText = (TextView)findViewById(R.id.player_score);
        playerScoreCircle.setVisibility(View.GONE);
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
        gameOverLayout = (LinearLayout)findViewById(R.id.layout_gameOver);
    }

    private void initListeners() {
        dealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dealNewRound();
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

        gameOverLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewGame();
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
        player.resetBalance();
        game.resetPlayersHands();
        balanceText.setText(String.valueOf(player.getBalance()));
        dealerScoreCircle.setVisibility(View.INVISIBLE);
        playerScoreCircle.setVisibility(View.INVISIBLE);
        currentBetText.setVisibility(View.INVISIBLE);
        currentBet = 10;
        betText.setText(String.valueOf(currentBet));
        enableDealButtons(true);
        gameOverLayout.setVisibility(View.INVISIBLE);
        dealerCardAdapter.notifyDataSetChanged();
        playerCardAdapter.notifyDataSetChanged();
    }

    private void dealNewRound() {
        game.dealAgain();
        player.bet(currentBet);

        dealerScoreCircle.setVisibility(View.VISIBLE);
        dealerScoreText.setText("?");
        playerScoreCircle.setVisibility(View.VISIBLE);
        playerScoreText.setText(String.valueOf(player.getHand().getScore()));

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
        dealerScoreText.setText(String.valueOf(dealer.getHand().getScore()));

        while (dealer.shouldDrawCard()) {
            game.dealCard(dealer);
            dealerCardAdapter.notifyDataSetChanged();
            dealerScoreText.setText(String.valueOf(dealer.getHand().getScore()));

            if (dealer.hasBusted()) {
                enableActionButtons(false);
                dealerBustResponse();
                return;
            }
        }

        Game.Outcome outcome = game.getOutcome();
        enableActionButtons(false);

        switch (outcome) {
            case DEALER:
                enableActionButtons(false);
                playerLoseResponse();
                break;
            case PLAYER:
                enableActionButtons(false);
                playerWinResponse();
                break;
            default:
                enableActionButtons(false);
                drawResponse();
        }
    }

    private void playerBlackjackResponse() {
        // TODO: Dealer blackjack?
        player.winBlackjack();
        resultText.setText(R.string.you_got_blackjack);
        showResult();
    }

    private void playerBustResponse() {
        if (player.isGameOver()) {
            gameOver();
            return;
        }
        resultText.setText(R.string.you_busted);
        showResult();
    }

    private void dealerBustResponse() {
        player.win();
        resultText.setText(R.string.dealer_busted);
        showResult();
    }

    private void drawResponse() {
        resultText.setText(R.string.draw);
        player.draw();
        resultLayout.setVisibility(View.VISIBLE);
    }

    private void playerWinResponse() {
        player.win();
        resultText.setText(R.string.you_won);
        showResult();
    }

    private void playerLoseResponse() {
        if (player.isGameOver()) {
            gameOver();
            return;
        }
        resultText.setText(R.string.dealer_won);
        showResult();
    }

    private void showResult() {
        resultLayout.setVisibility(View.VISIBLE);
        balanceText.setText(String.valueOf(player.getBalance()));
    }

    private void gameOver() {
        gameOverLayout.setVisibility(View.VISIBLE);
        enableActionButtons(false);
        enableDealButtons(false);
    }

    private void resetGame() {
        game.resetPlayersHands();
        currentBet = 10; // Eller beholde samme bet? Evt sette ned til lik balance
        betText.setText(String.valueOf(currentBet));

        dealerCardAdapter.notifyDataSetChanged();
        playerCardAdapter.notifyDataSetChanged();

        dealerScoreCircle.setVisibility(View.INVISIBLE);
        playerScoreCircle.setVisibility(View.INVISIBLE);
        dealerScoreText.setText("?");
        playerScoreText.setText("");
        balanceText.setText(String.valueOf(player.getBalance()));
        currentBetText.setVisibility(View.GONE);

        resultLayout.setVisibility(View.INVISIBLE);

        enableActionButtons(false);
        enableDealButtons(true);

        if (game.playerBlackjack()) {
            enableActionButtons(false);
            playerBlackjackResponse();
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
