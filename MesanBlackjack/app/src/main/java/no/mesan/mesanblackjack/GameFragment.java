package no.mesan.mesanblackjack;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public GameFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        RecyclerView dealerRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerView_dealer);
        RecyclerView playerRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerView_player);
        setupRecyclerView(dealerRecyclerView);
        setupRecyclerView(playerRecyclerView);

        Game game = new Game();
        Dealer dealer = game.getDealer();
        Player player = game.getPlayer();

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
