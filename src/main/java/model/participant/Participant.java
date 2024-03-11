package model.participant;

import model.card.Card;
import model.card.CardDeck;
import model.dto.FaceUpResult;

public abstract class Participant {

    private final Name name;
    protected final CardDeck cardDeck;

    protected Participant(Name name) {
        this.name = name;
        this.cardDeck = new CardDeck();
    }

    public abstract boolean canHit();

    public void hitCard(Card card) {
        cardDeck.addCard(card);
    }

    public FaceUpResult generateFaceUpResult() {
        return new FaceUpResult(name, cardDeck.getCards(), cardDeck.calculateHand());
    }

}
