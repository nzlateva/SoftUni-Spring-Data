package bg.softuni.springdata.automapping.model.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity{

    private User buyer;
    private Set<Game> games;

    public Order() {
        this.games = new HashSet<>();
    }

    @ManyToOne
    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    // only one order can contain one or many games,
    // because a game cannot be bought for second time
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    public Set<Game> getGames() {
        return games;
    }

    public void setGames(Set<Game> games) {
        this.games = games;
    }
}
