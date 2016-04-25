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
    private TextView dealerScoreText, playerScoreText, currentBetText, balanceText, betText, resultText;
    private Button hitButton, standButton, minusButton, plusButton, dealButton;
    private LinearLayout dealerScoreCircle, playerScoreCircle, resultLayout, gameOverLayout;

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

        dealerScoreText = (TextView)findViewById(R.id.dealer_score);
        playerScoreText = (TextView)findViewById(R.id.player_score);
        currentBetText = (TextView)findViewById(R.id.txt_currentBet);
        currentBetText.setVisibility(View.GONE);
        balanceText = (TextView)findViewById(R.id.txt_balance);
        balanceText.setText(String.valueOf(player.getBalance()));
        betText = (TextView)findViewById(R.id.txt_bet);
        betText.setText(String.valueOf(currentBet));
        resultText = (TextView)findViewById(R.id.txt_result);

        hitButton = (Button)findViewById(R.id.btn_hit);
        standButton = (Button)findViewById(R.id.btn_stand);
        minusButton = (Button)findViewById(R.id.btn_minus);
        minusButton.setEnabled(false);
        plusButton = (Button)findViewById(R.id.btn_plus);
        dealButton = (Button)findViewById(R.id.btn_deal);

        dealerScoreCircle = (LinearLayout)findViewById(R.id.dealer_score_holder);
        dealerScoreCircle.setVisibility(View.GONE);
        playerScoreCircle = (LinearLayout)findViewById(R.id.player_score_holder);
        playerScoreCircle.setVisibility(View.GONE);
        resultLayout = (LinearLayout)findViewById(R.id.layout_result);
        gameOverLayout = (LinearLayout)findViewById(R.id.layout_gameOver);
    }

    private void initListeners() {
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

        dealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dealNewRound();
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
        int minimumBet = 10;
        if (currentBet > minimumBet) {
            currentBet -= 10;
            betText.setText(String.valueOf(currentBet));

            plusButton.setEnabled(true);
            if (currentBet == minimumBet) {
                minusButton.setEnabled(false);
            }
        }
    }

    private void increaseBet() {
        int balance = player.getBalance();
        if (currentBet <= balance - 10) {
            currentBet += 10;
            betText.setText(String.valueOf(currentBet));

            minusButton.setEnabled(true);
            if (currentBet == balance) {
                plusButton.setEnabled(false);
            }

        }
    }

    private void startNewGame() {
        player.resetBalance();
        game.resetPlayersHands();
        currentBet = 10;

        dealerCardAdapter.notifyDataSetChanged();
        playerCardAdapter.notifyDataSetChanged();

        currentBetText.setVisibility(View.INVISIBLE);
        balanceText.setText(String.valueOf(player.getBalance()));
        betText.setText(String.valueOf(currentBet));

        enableDealButtons(true);

        dealerScoreCircle.setVisibility(View.INVISIBLE);
        playerScoreCircle.setVisibility(View.INVISIBLE);
        gameOverLayout.setVisibility(View.INVISIBLE);
    }

    private void dealNewRound() {
        game.dealAgain();
        player.bet(currentBet);

        dealerCardAdapter = new CardAdapter(dealer.getHand());
        playerCardAdapter = new CardAdapter(player.getHand());

        dealerRecyclerView.setAdapter(dealerCardAdapter);
        playerRecyclerView.setAdapter(playerCardAdapter);

        dealerScoreText.setText("?");
        playerScoreText.setText(String.valueOf(player.getHand().getScore()));
        balanceText.setText(String.valueOf(player.getBalance()));
        currentBetText.setText(String.valueOf(currentBet));
        currentBetText.setVisibility(View.VISIBLE);

        enableActionButtons(true);
        enableDealButtons(false);

        dealerScoreCircle.setVisibility(View.VISIBLE);
        playerScoreCircle.setVisibility(View.VISIBLE);

        if (game.playerBlackjack()) {
            enableActionButtons(false);
            playerBlackjackResponse();
            return;
        }
    }

    private void playerHits() {
        game.dealCard(player);

        playerCardAdapter.notifyDataSetChanged();
        playerScoreText.setText(String.valueOf(player.getHand().getScore()));

        if (player.hasBlackjack()) {
            playerStands();
            enableActionButtons(false);
        } else if (game.playerBust()) {
            playerBustResponse();
            enableActionButtons(false);
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
                dealerBustResponse();
                enableActionButtons(false);
                return;
            }
        }

        Game.Outcome outcome = game.getOutcome();
        enableActionButtons(false);

        switch (outcome) {
            case DEALER:
                playerLoseResponse();
                enableActionButtons(false);
                break;
            case PLAYER:
                playerWinResponse();
                enableActionButtons(false);
                break;
            default:
                drawResponse();
                enableActionButtons(false);
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
        currentBet = 10;

        dealerCardAdapter.notifyDataSetChanged();
        playerCardAdapter.notifyDataSetChanged();

        dealerScoreText.setText("?");
        playerScoreText.setText("");
        balanceText.setText(String.valueOf(player.getBalance()));
        currentBetText.setVisibility(View.GONE);
        betText.setText(String.valueOf(currentBet));

        enableActionButtons(false);
        enableDealButtons(true);

        dealerScoreCircle.setVisibility(View.INVISIBLE);
        playerScoreCircle.setVisibility(View.INVISIBLE);
        resultLayout.setVisibility(View.INVISIBLE);

        if (game.playerBlackjack()) {
            playerBlackjackResponse();
            enableActionButtons(false);
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
