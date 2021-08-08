package entities.football;

import entities.BaseEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "players")
public class Player extends BaseEntity {

    private String name;
    private Integer squadNumber;
    private Team team;
    private Position position;
    private Boolean isCurrentlyInjured;
    private Set<PlayerStatistic> playerStatistics;

    public Player() {
        this.playerStatistics = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "squad_number")
    public Integer getSquadNumber() {
        return squadNumber;
    }

    public void setSquadNumber(Integer squadNumber) {
        this.squadNumber = squadNumber;
    }

    @ManyToOne
    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @ManyToOne
    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Column(name = "is_currently_injured")
    public Boolean getCurrentlyInjured() {
        return isCurrentlyInjured;
    }

    public void setCurrentlyInjured(Boolean currentlyInjured) {
        isCurrentlyInjured = currentlyInjured;
    }

    @OneToMany(mappedBy = "player")
    public Set<PlayerStatistic> getPlayerStatistics() {
        return playerStatistics;
    }

    public void setPlayerStatistics(Set<PlayerStatistic> playerStatistics) {
        this.playerStatistics = playerStatistics;
    }
}
