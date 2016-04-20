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
    protected final int VIEW_TYPE_SMALL = 3;

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
            /*case VIEW_TYPE_SMALL:
                View v1 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.small_card_view, parent, false);
                return new SmallCardViewHolder(v1);*/
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

        if (holder instanceof SmallCardViewHolder) {
            SmallCardViewHolder viewHolder = (SmallCardViewHolder)holder;
            viewHolder.upperValueTextView.setText(value);
            viewHolder.upperSuitImageView.setImageResource(resource);
        }

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
        //if (lastCard(position)) {
            return hand.getCards().get(position).isVisible() ?
                    VIEW_TYPE_OPEN : VIEW_TYPE_CLOSED;
        //}
        //return VIEW_TYPE_SMALL;
    }

    private boolean lastCard(int position) {
        return position == getItemCount() - 1;
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        public CardViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class SmallCardViewHolder extends CardViewHolder {
        TextView upperValueTextView;
        ImageView upperSuitImageView;

        public SmallCardViewHolder(View itemView) {
            super(itemView);
            upperValueTextView = (TextView)itemView.findViewById(R.id.txt_value_upper);
            upperSuitImageView = (ImageView)itemView.findViewById(R.id.img_suit_upper);
        }
    }

    public class OpenCardViewHolder extends SmallCardViewHolder {
        TextView lowerValueTextView;
        ImageView lowerSuitImageView;

        public OpenCardViewHolder(View itemView) {
            super(itemView);
            lowerValueTextView = (TextView)itemView.findViewById(R.id.txt_value_lower);
            lowerSuitImageView = (ImageView)itemView.findViewById(R.id.img_suit_lower);
        }
    }
}
