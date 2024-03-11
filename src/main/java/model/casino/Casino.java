package model.casino;

import static java.util.Collections.*;

import java.util.EnumMap;
import java.util.List;
import model.Choice;
import model.dto.DealerScoreResult;
import model.dto.GameCompletionResult;
import model.dto.PlayerScoreResult;
import view.Victory;
import model.participant.Participants;

public class Casino {
    private final Participants participants;
    private final CardDispenser cardDispenser;

    public Casino(Participants participants, CardShuffleMachine cardShuffleMachine) {
        this.participants = participants;
        this.cardDispenser = new CardDispenser(cardShuffleMachine);
    }

    public void hitCardTwice() {
        int playerSize = participants.getPlayerSize();
        for (int i = 0; i < playerSize; i++) {
            participants.hitAndMoveToNextPlayer(cardDispenser.dispenseCard());
            participants.hitAndMoveToNextPlayer(cardDispenser.dispenseCard());
        }
        hitCardToDealer();
        hitCardToDealer();
    }

    public void distinctPlayerChoice(Choice choice) {
        if (choice.isYes()) {
            hitCardToPlayer();
            return;
        }
        participants.turnOverPlayer();
    }

    private void hitCardToPlayer() {
        participants.hitPlayer(cardDispenser.dispenseCard());
    }

    public void hitCardToDealer() {
        participants.hitDealer(cardDispenser.dispenseCard());
    }

    public boolean hasAvailablePlayer() {
        return participants.hasAvailablePlayer();
    }

    public boolean isDealerHitAllowed() {
        return participants.canHitDealer();
    }

    public GameCompletionResult getDealerFaceUpResult() {
        return participants.getDealerFaceUpResult();
    }

    public List<GameCompletionResult> getPlayerFaceUpResult() {
        return participants.getPlayerFaceUpResults();
    }

    public GameCompletionResult getNextPlayerFaceUpInfo() {
        return participants.getNextAvailablePlayerName();
    }

    public List<PlayerScoreResult> calculatePlayerResults() {
        int dealerHand = participants.getDealerFaceUpResult()
                .hand();
        return participants.getPlayerFaceUpResults()
                .stream()
                .map(result -> new PlayerScoreResult(result.name(), Victory.of(result.hand(), dealerHand)))
                .toList();
    }

    public DealerScoreResult calculateDealerResult() {
        int dealerHand = participants.getDealerFaceUpResult().hand();
        EnumMap<Victory, Integer> dealerScoreBoard = new EnumMap<>(Victory.class);
        List<Victory> dealerScores = participants.getPlayerFaceUpResults()
                .stream()
                .map(player -> Victory.of(dealerHand, player.hand()))
                .toList();
        for (Victory victory : Victory.values()) {
            dealerScoreBoard.put(victory, frequency(dealerScores, victory));
        }
        return new DealerScoreResult(dealerScoreBoard);
    }

}
