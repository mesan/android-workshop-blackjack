package no.mesan.mesanblackjack;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import no.mesan.mesanblackjack.adapter.CardAdapter;
import no.mesan.mesanblackjack.adapter.ItemDecorator;
import no.mesan.mesanblackjack.model.Dealer;
import no.mesan.mesanblackjack.model.Game;
import no.mesan.mesanblackjack.model.Player;

public class GameActivity extends AppCompatActivity {

    private Game game;
    private Dealer dealer;
    private Player player;

    int currentBet = 10;

    private RecyclerView dealerRecyclerView, playerRecyclerView;
    private CardAdapter dealerCardAdapter, playerCardAdapter;
    TextView balanceText, dealerScoreText, playerScoreText, currentBetText, betText;
    Button hitButton, standButton, dealButton, minusButton, plusButton;

    private Handler handler = new Handler();

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

        balanceText = (TextView)findViewById(R.id.txt_money);
        dealerScoreText = (TextView)findViewById(R.id.dealer_score);
        playerScoreText = (TextView)findViewById(R.id.player_score);
        currentBetText = (TextView)findViewById(R.id.txt_currentBet);
        currentBetText.setVisibility(View.GONE);
        betText = (TextView)findViewById(R.id.txt_bet);
        betText.setText(String.valueOf(currentBet));

        hitButton = (Button)findViewById(R.id.btn_hit);
        standButton = (Button)findViewById(R.id.btn_stand);
        dealButton = (Button)findViewById(R.id.btn_deal);
        minusButton = (Button)findViewById(R.id.btn_minus);
        minusButton.setEnabled(false);
        plusButton = (Button)findViewById(R.id.btn_plus);
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

        // Sjekk om umiddelbar blackjack
        // Ulik payback hvis første eller senere?
        if (game.playerBlackjack()) {
            enableActionButtons(false);
            delayPlayerBlackjackResponse();
            return;
        }

        // TODO: Sjekk om to like
        // TODO: Sjekk om dealer har A som åpent kort

        enableActionButtons(true);
        enableDealButtons(false);
    }

    private void enableDealButtons(boolean enabled) {
        dealButton.setEnabled(enabled);
        minusButton.setEnabled(enabled);
        plusButton.setEnabled(enabled);
    }

    private void playerHits() {
        game.dealCard(player);
        playerScoreText.setText(String.valueOf(player.getHand().getScore()));
        playerCardAdapter.notifyDataSetChanged();

        if (player.hasBlackjack()) {
            enableActionButtons(false);
            delayPlayerBlackjackResponse();
        } else if (game.playerBust()) {
            enableActionButtons(false);
            delayPlayerBustResponse();
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
                delayDealerBustResponse();
                return;
            }
        }

        if (player.getHand().getScore() == dealer.getHand().getScore()) {
            enableActionButtons(false);
            delayDrawResponse();
        } else if (player.getHand().getScore() > dealer.getHand().getScore()) {
            enableActionButtons(false);
            delayPlayerWinResponse();
        } else if (player.getHand().getScore() < dealer.getHand().getScore()) {
            enableActionButtons(false);
            delayPlayerLoseResponse();
        }
    }

    private void delayPlayerBlackjackResponse() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                playerBlackjackResponse();
            }
        }, 3000);
    }

    private void delayPlayerBustResponse() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                playerBustResponse();
            }
        }, 3000);
    }

    private void delayDealerBustResponse() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dealerBustResponse();
            }
        }, 3000);
    }

    private void delayDrawResponse() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                drawResponse();
            }
        }, 3000);
    }

    private void delayPlayerWinResponse() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                playerWinResponse();
            }
        }, 3000);
    }

    private void delayPlayerLoseResponse() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                playerLoseResponse();
            }
        }, 3000);
    }

    private void playerBlackjackResponse() {
        // TODO: Dealer blackjack?
        player.win();
        Toast.makeText(this, "Blackjack!", Toast.LENGTH_SHORT).show();
        resetGame();
    }

    private void playerBustResponse() {
        Toast.makeText(this, "Bust!", Toast.LENGTH_SHORT).show();
        resetGame();
    }

    private void dealerBustResponse() {
        player.win();
        Toast.makeText(this, "Dealer bust!", Toast.LENGTH_SHORT).show();
        resetGame();
    }

    private void drawResponse() {
        player.draw();
        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
        resetGame();
    }

    private void playerWinResponse() {
        player.win();
        Toast.makeText(this, "Win!", Toast.LENGTH_SHORT).show();
        resetGame();
    }

    private void playerLoseResponse() {
        Toast.makeText(this, "Loss!", Toast.LENGTH_SHORT).show();
        resetGame();
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

        enableActionButtons(false);
        enableDealButtons(true);
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
