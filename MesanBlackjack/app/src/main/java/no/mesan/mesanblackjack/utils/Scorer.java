package no.mesan.mesanblackjack.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import no.mesan.mesanblackjack.model.Card;

/**
 * Created by mikkels on 19/04/16.
 */
public class Scorer {
    private final static Scorer instance = new Scorer();

    private Scorer() {}

    public static Scorer getInstance() { return instance; }

    public int calculate(List<Card> cards) {
        int sum = 0;
        Collections.sort(cards, new Comparator<Card>() {
            @Override
            public int compare(Card lhs, Card rhs) {
                return rhs.getValue().getPoints() - lhs.getValue().getPoints();
            }
        });
        for (Card card : cards) {
            if (card.getValue().getValue() == 1) {
                if (sum > 10) {
                    sum += 1;
                } else {
                    sum += 11;
                }
            } else {
                sum += card.getValue().getPoints();
            }
        }
        return sum;
    }
}
