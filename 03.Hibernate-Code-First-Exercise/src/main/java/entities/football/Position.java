package entities.football;

import entities.football.enums.PositionDescription;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "positions")
public class Position {

    private String id;
    private PositionDescription positionDescription;
    private Set<Player> players;

    public Position() {
        this.players = new HashSet<>();
    }

    @Id
    @Column(length = 2, nullable = false)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "position_description")
    public PositionDescription getPositionDescription() {
        return positionDescription;
    }

    public void setPositionDescription(PositionDescription positionDescription) {
        this.positionDescription = positionDescription;
    }

    @OneToMany(mappedBy = "position")
    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }
}
