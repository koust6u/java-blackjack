package model.dto;

import java.util.EnumMap;
import view.Victory;

public record DealerScoreResult(EnumMap<Victory, Integer> scoreStorage) {
    public String getVictoryScoreAsString(Victory victory){
        return scoreStorage.get(victory) + victory.getValue();
    }
}
