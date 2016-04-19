package no.mesan.mesanblackjack.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import no.mesan.mesanblackjack.R;
import no.mesan.mesanblackjack.model.Card;
import no.mesan.mesanblackjack.model.Hand;
import no.mesan.mesanblackjack.model.Suit;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private Hand hand;

    // TODO: Int to typer open/closed card

    public CardAdapter(Hand hand) {
        this.hand = hand;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);
        return new CardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        Card card = hand.getCards().get(position);
        String value = ""+ card.getValue().getValue();
        Suit suit = card.getSuit();
        int resource = getDrawableFromSuit(suit);
        holder.upperValueTextView.setText(value);
        holder.lowerValueTextView.setText(value);
        holder.upperSuitImageView.setImageResource(resource);
        holder.lowerSuitImageView.setImageResource(resource);
    }

    private int getDrawableFromSuit(Suit suit) {
        switch (suit) {
            case HEARTS:
                return R.drawable.hearts;
            case SPADES:
                return R.drawable.spades;
            case DIAMONDS:
                return R.drawable.diamonds;
            case CLUBS:
                return R.drawable.clubs;
            default:
                return R.drawable.hearts;
        }
    }

    @Override
    public int getItemCount() {
        return hand.getCards().size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        TextView upperValueTextView;
        ImageView upperSuitImageView;
        TextView lowerValueTextView;
        ImageView lowerSuitImageView;

        public CardViewHolder(View itemView) {
            super(itemView);
            upperValueTextView = (TextView)itemView.findViewById(R.id.txt_value_upper);
            upperSuitImageView = (ImageView)itemView.findViewById(R.id.img_suit_upper);
            lowerValueTextView = (TextView)itemView.findViewById(R.id.txt_value_lower);
            lowerSuitImageView = (ImageView)itemView.findViewById(R.id.img_suit_lower);
        }
    }
}
