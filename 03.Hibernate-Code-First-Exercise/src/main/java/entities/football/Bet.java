package entities.football;

import entities.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "bets")
public class Bet extends BaseEntity {

    private BigDecimal betMoney;
    private LocalDateTime dateTimeOfBet;
    private BetUser user;
    private Set<BetGame> betGames;

    public Bet() {
        this.betGames = new HashSet<>();
    }

    @Column(name = "bet_money")
    public BigDecimal getBetMoney() {
        return betMoney;
    }

    public void setBetMoney(BigDecimal betMoney) {
        this.betMoney = betMoney;
    }

    @Column(name = "date_time")
    public LocalDateTime getDateTimeOfBet() {
        return dateTimeOfBet;
    }

    public void setDateTimeOfBet(LocalDateTime dateTimeOfBet) {
        this.dateTimeOfBet = dateTimeOfBet;
    }

    @ManyToOne
    public BetUser getUser() {
        return user;
    }

    public void setUser(BetUser user) {
        this.user = user;
    }

    @OneToMany(mappedBy = "bet")
    public Set<BetGame> getBetGames() {
        return betGames;
    }

    public void setBetGames(Set<BetGame> betGames) {
        this.betGames = betGames;
    }
}
