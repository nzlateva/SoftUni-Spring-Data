package bg.softuni.springdata.automapping.service;

import bg.softuni.springdata.automapping.model.dto.AddGameDto;
import bg.softuni.springdata.automapping.model.dto.EditGameDto;
import bg.softuni.springdata.automapping.model.dto.GameTitleAndPriceViewDto;
import bg.softuni.springdata.automapping.model.dto.GameViewDto;
import bg.softuni.springdata.automapping.model.entity.Game;

import java.util.List;

public interface GameService {

    String addGame(AddGameDto addGameDto);

    String editGame(EditGameDto editGameDto);

    String deleteGame(Long id);

    List<GameTitleAndPriceViewDto> findTitlesAndPriceOfAllGames();

    GameViewDto findGame(String gameTitle);

    Game findByTitleAndUserIsNull(String gameTitle);

    String purchaseGame(Game game);

    void save(Game game);
}
