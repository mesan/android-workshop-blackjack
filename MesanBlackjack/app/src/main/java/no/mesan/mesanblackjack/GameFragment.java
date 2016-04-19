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

import no.mesan.mesanblackjack.adapter.CardAdapter;
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

    private RecyclerView dealerRecyclerView;
    private RecyclerView playerRecyclerView;

    public GameFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        Button drawButton = (Button)view.findViewById(R.id.btn_hit);
        drawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.dealCard(player);
                playerRecyclerView.setAdapter(new CardAdapter(player.getHand()));
            }
        });

        dealerRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerView_dealer);
        playerRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerView_player);
        setupRecyclerView(dealerRecyclerView);
        setupRecyclerView(playerRecyclerView);

        game = new Game();
        dealer = game.getDealer();
        player = game.getPlayer();

        dealerRecyclerView.setAdapter(new CardAdapter(dealer.getHand()));
        playerRecyclerView.setAdapter(new CardAdapter(player.getHand()));

        return view;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(llm);
        //recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
    }
}
