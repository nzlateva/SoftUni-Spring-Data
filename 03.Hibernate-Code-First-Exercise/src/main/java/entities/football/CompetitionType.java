package entities.football;

import entities.BaseEntity;
import entities.football.enums.CompetitionName;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "competition_types")
public class CompetitionType extends BaseEntity {

    private CompetitionName name;
    private Set<Competition> competitions;

    public CompetitionType() {
        this.competitions = new HashSet<>();
    }

    @Enumerated(EnumType.STRING)
    public CompetitionName getName() {
        return name;
    }

    public void setName(CompetitionName name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "competitionType")
    public Set<Competition> getCompetitions() {
        return competitions;
    }

    public void setCompetitions(Set<Competition> competitions) {
        this.competitions = competitions;
    }
}
