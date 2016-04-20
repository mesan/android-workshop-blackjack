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

    private CardAdapter playerCardAdapter;
    private CardAdapter dealerCardAdapter;

    TextView textViewMoney, dealerScoreText, playerScoreText;

    public GameFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        dealerScoreText = (TextView)view.findViewById(R.id.dealer_score);
        playerScoreText = (TextView)view.findViewById(R.id.player_score);

        Button hitButton = (Button)view.findViewById(R.id.btn_hit);
        hitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.dealCard(player);
                playerScoreText.setText(Integer.toString(player.getHand().getScore()));
                playerCardAdapter.notifyDataSetChanged();
            }
        });

        Button standButton = (Button)view.findViewById(R.id.btn_stand);
        standButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Deal cards till bust or win
                game.dealCard(dealer);
                dealerScoreText.setText(Integer.toString(dealer.getHand().getScore()));
                dealerCardAdapter.notifyDataSetChanged();
            }
        });

        RecyclerView dealerRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_dealer);
        RecyclerView playerRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_player);
        setupRecyclerView(dealerRecyclerView);
        setupRecyclerView(playerRecyclerView);

        game = new Game();
        dealer = game.getDealer();
        player = game.getPlayer();

        textViewMoney = (TextView)view.findViewById(R.id.txt_money);
        textViewMoney.setText("" + player.getMoney());

        dealerCardAdapter = new CardAdapter(dealer.getHand());
        playerCardAdapter = new CardAdapter(player.getHand());

        dealerRecyclerView.setAdapter(dealerCardAdapter);
        playerRecyclerView.setAdapter(playerCardAdapter);

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
