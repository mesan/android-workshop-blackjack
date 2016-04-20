package no.mesan.mesanblackjack;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

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

    TextView textViewMoney, dealerScoreText, playerScoreText, currentBetText;
    Button hitButton, standButton, dealButton;

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
        currentBetText.setVisibility(View.GONE);
        final TextView betText = (TextView)view.findViewById(R.id.txt_bet);
        hitButton = (Button)view.findViewById(R.id.btn_hit);
        standButton = (Button)view.findViewById(R.id.btn_stand);
        dealButton = (Button)view.findViewById(R.id.btn_deal);
        final Button plusButton = (Button)view.findViewById(R.id.btn_plus);
        final Button minusButton = (Button)view.findViewById(R.id.btn_minus);
        dealerRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_dealer);
        playerRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_player);

        betText.setText(String.valueOf(currentBet));
        minusButton.setEnabled(false);

        setupRecyclerView(dealerRecyclerView);
        setupRecyclerView(playerRecyclerView);

        dealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Disable deal buttons until new game start
                player.bet(currentBet);
                dealerCardAdapter = new CardAdapter(dealer.getHand());
                playerCardAdapter = new CardAdapter(player.getHand());

                dealerRecyclerView.setAdapter(dealerCardAdapter);
                playerRecyclerView.setAdapter(playerCardAdapter);

                currentBetText.setText(String.valueOf(currentBet));
                currentBetText.setVisibility(View.VISIBLE);
                textViewMoney.setText(String.valueOf(player.getMoney()));
            }
        });

        hitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.dealCard(player);
                playerScoreText.setText(Integer.toString(player.getHand().getScore()));
                playerCardAdapter.notifyDataSetChanged();
            }
        });

        standButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Deal cards till bust or win
                dealer.showHoleCard();
                game.dealCard(dealer);
                dealerScoreText.setText(Integer.toString(dealer.getHand().getScore()));
                dealerCardAdapter.notifyDataSetChanged();
            }
        });

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentBet == player.getMoney()) {
                    plusButton.setEnabled(false);
                } else {
                    minusButton.setEnabled(true);
                    currentBet += 10;
                    if (currentBet == player.getMoney()) {
                        plusButton.setEnabled(false);
                    }
                    betText.setText(String.valueOf(currentBet));
                }
            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentBet == 10) {
                    minusButton.setEnabled(false);
                } else {
                    plusButton.setEnabled(true);
                    currentBet -= 10;
                    if (currentBet == 10) {
                        minusButton.setEnabled(false);
                    }
                    betText.setText(String.valueOf(currentBet));
                }
            }
        });

        return view;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(new ItemDecorator(-100));
    }
}
