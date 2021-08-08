package bg.softuni.springdata.automapping.repository;

import bg.softuni.springdata.automapping.model.entity.Game;
import bg.softuni.springdata.automapping.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    Game findByTitle(String title);

    Game findByTitleAndUserIsNull(String title);
}
