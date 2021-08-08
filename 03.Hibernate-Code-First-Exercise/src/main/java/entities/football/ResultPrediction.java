package entities.football;

import entities.BaseEntity;
import entities.football.enums.Prediction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "result_predictions")
public class ResultPrediction extends BaseEntity {

    private Prediction prediction;
    private Set<BetGame> betGames;

    public ResultPrediction() {
        this.betGames = new HashSet<>();
    }

    @Enumerated(EnumType.STRING)
    public Prediction getPrediction() {
        return prediction;
    }

    public void setPrediction(Prediction prediction) {
        this.prediction = prediction;
    }

    @OneToMany(mappedBy = "resultPrediction")
    public Set<BetGame> getBetGames() {
        return betGames;
    }

    public void setBetGames(Set<BetGame> betGames) {
        this.betGames = betGames;
    }
}
