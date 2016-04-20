package no.mesan.mesanblackjack;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import no.mesan.mesanblackjack.adapter.CardAdapter;
import no.mesan.mesanblackjack.adapter.ItemDecorator;
import no.mesan.mesanblackjack.model.Dealer;
import no.mesan.mesanblackjack.model.Game;
import no.mesan.mesanblackjack.model.Player;

/**
 * A placeholder fragment containing a simple view.
 */
public class GameFragment extends Fragment {

    private Game game;
    private Dealer dealer;
    private Player player;

    private RecyclerView playerRecyclerView;
    private RecyclerView dealerRecyclerView;
    private CardAdapter playerCardAdapter;
    private CardAdapter dealerCardAdapter;

    int currentBet = 10;

    TextView textViewMoney, dealerScoreText, playerScoreText, currentBetText, betText;
    Button hitButton, standButton, dealButton, minusButton, plusButton;

    public GameFragment() {
        game = new Game();
        dealer = game.getDealer();
        player = game.getPlayer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        dealerScoreText = (TextView)view.findViewById(R.id.dealer_score);
        playerScoreText = (TextView)view.findViewById(R.id.player_score);
        textViewMoney = (TextView)view.findViewById(R.id.txt_money);
        currentBetText = (TextView)view.findViewById(R.id.txt_currentBet);
        betText = (TextView)view.findViewById(R.id.txt_bet);
        hitButton = (Button)view.findViewById(R.id.btn_hit);
        standButton = (Button)view.findViewById(R.id.btn_stand);
        dealButton = (Button)view.findViewById(R.id.btn_deal);
        plusButton = (Button)view.findViewById(R.id.btn_plus);
        minusButton = (Button)view.findViewById(R.id.btn_minus);
        dealerRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_dealer);
        playerRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_player);

        currentBetText.setVisibility(View.GONE);

        betText.setText(String.valueOf(currentBet));
        minusButton.setEnabled(false);

        setupRecyclerView(dealerRecyclerView);
        setupRecyclerView(playerRecyclerView);

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

        return view;
    }

    private void decreaseBet() {
        if (currentBet == 10) {
            return;
        }

        plusButton.setEnabled(true);
        currentBet -= 10;
        if (currentBet == 10) {
            minusButton.setEnabled(false);
        }
        betText.setText(String.valueOf(currentBet));
    }

    private void increaseBet() {
        if (currentBet == player.getMoney()) {
            return;
        }

        minusButton.setEnabled(true);
        currentBet += 10;
        if (currentBet == player.getMoney()) {
            plusButton.setEnabled(false);
        }
        betText.setText(String.valueOf(currentBet));
    }

    private void startNewGame() {
        minusButton.setEnabled(false);
        plusButton.setEnabled(false);
        dealButton.setEnabled(false);

        game.dealAgain();
        player.bet(currentBet);
        dealerCardAdapter = new CardAdapter(dealer.getHand());
        playerCardAdapter = new CardAdapter(player.getHand());

        dealerRecyclerView.setAdapter(dealerCardAdapter);
        playerRecyclerView.setAdapter(playerCardAdapter);

        currentBetText.setText(String.valueOf(currentBet));
        currentBetText.setVisibility(View.VISIBLE);
        textViewMoney.setText(String.valueOf(player.getMoney()));

        hitButton.setEnabled(true);
        standButton.setEnabled(true);
    }

    private void playerHits() {
        game.dealCard(player);
        playerScoreText.setText(Integer.toString(player.getHand().getScore()));
        playerCardAdapter.notifyDataSetChanged();
        if (player.hasBlackjack()) {
            player.win();
            resetGame();
        } else if (game.playerBust()) {
            resetGame();
        }
    }

    private void playerStands() {
        // TODO: Deal cards till bust or win
        dealer.showHoleCard();
        game.dealCard(dealer);
        dealerScoreText.setText(Integer.toString(dealer.getHand().getScore()));
        dealerCardAdapter.notifyDataSetChanged();
    }

    private void resetGame() {
        currentBet = 10;
        minusButton.setEnabled(false);
        game.emptyHands();
        playerCardAdapter.notifyDataSetChanged();
        dealerCardAdapter.notifyDataSetChanged();
        currentBetText.setVisibility(View.GONE);
        textViewMoney.setText(String.valueOf(player.getMoney()));
        hitButton.setEnabled(false);
        standButton.setEnabled(false);
        plusButton.setEnabled(true);
        minusButton.setEnabled(true);
        dealButton.setEnabled(true);
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(new ItemDecorator(-100));
    }
}
