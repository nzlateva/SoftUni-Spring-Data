package entities.football;

import entities.BaseEntity;
import entities.football.enums.RoundName;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "rounds")
public class Round extends BaseEntity {

    private RoundName name;
    private Set<Game> games;

    public Round() {
        this.games = new HashSet<>();
    }

    @Enumerated(EnumType.STRING)
    public RoundName getName() {
        return name;
    }

    public void setName(RoundName name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "round")
    public Set<Game> getGames() {
        return games;
    }

    public void setGames(Set<Game> games) {
        this.games = games;
    }
}
