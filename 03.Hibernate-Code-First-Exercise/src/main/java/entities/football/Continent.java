package entities.football;

import entities.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "continents")
public class Continent extends BaseEntity {

    private String name;
    private Set<Country> counties;

    public Continent() {
        this.counties = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany(mappedBy = "continents")
    public Set<Country> getCounties() {
        return counties;
    }

    public void setCounties(Set<Country> counties) {
        this.counties = counties;
    }
}
