package machineLearning.positionTrainer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by patry on 21/05/17.
 */
public class PositionTrainerSelector {

    private static PositionTrainerSelector instance = new PositionTrainerSelector();

    public static PositionTrainerSelector getInstance() {
        return instance;
    }

    private Map<PositionTrainerType, PositionTrainer> trainers;

    private PositionTrainerSelector() {
        trainers = new HashMap<>();
        trainers.put(PositionTrainerType.random, new RandomTrainer());
        trainers.put(PositionTrainerType.kMeans, new KMeansTrainer());
    }

    public PositionTrainer selectTrainer(PositionTrainerType type) {
        return trainers.get(type);
    }
}
