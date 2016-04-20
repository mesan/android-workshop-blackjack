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

    protected final int VIEW_TYPE_OPEN = 1;
    protected final int VIEW_TYPE_CLOSED = 2;

    public CardAdapter(Hand hand) {
        this.hand = hand;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_OPEN:
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view, parent, false);
                return new OpenCardViewHolder(v);
            default:
                View v2 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.closed_card_view, parent, false);
                return new CardViewHolder(v2);
        }
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        Card card = hand.getCards().get(position);
        String value = card.getValue().getName();
        Suit suit = card.getSuit();
        int resource = getImageResource(suit);

        if (holder instanceof OpenCardViewHolder) {
            OpenCardViewHolder viewHolder = (OpenCardViewHolder)holder;
            viewHolder.lowerValueTextView.setText(value);
            viewHolder.lowerSuitImageView.setImageResource(resource);
        }
    }

    private int getImageResource(Suit suit) {
        switch (suit) {
            case HEARTS:
                return R.drawable.hearts;
            case DIAMONDS:
                return R.drawable.diamonds;
            case CLUBS:
                return R.drawable.clubs;
            default:
                return R.drawable.spades;
        }
    }

    @Override
    public int getItemCount() {
        return hand.getCards().size();
    }

    @Override
    public int getItemViewType(int position) {
        return hand.getCards().get(position).isVisible() ?
                    VIEW_TYPE_OPEN : VIEW_TYPE_CLOSED;
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        public CardViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class OpenCardViewHolder extends CardViewHolder {
        TextView upperValueTextView;
        ImageView upperSuitImageView;
        TextView lowerValueTextView;
        ImageView lowerSuitImageView;

        public OpenCardViewHolder(View itemView) {
            super(itemView);
            upperValueTextView = (TextView)itemView.findViewById(R.id.txt_value_upper);
            upperSuitImageView = (ImageView)itemView.findViewById(R.id.img_suit_upper);
            lowerValueTextView = (TextView)itemView.findViewById(R.id.txt_value_lower);
            lowerSuitImageView = (ImageView)itemView.findViewById(R.id.img_suit_lower);
        }
    }
}
