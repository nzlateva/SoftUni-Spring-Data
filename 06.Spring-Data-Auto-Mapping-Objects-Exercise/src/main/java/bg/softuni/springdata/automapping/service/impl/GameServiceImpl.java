package bg.softuni.springdata.automapping.service.impl;

import bg.softuni.springdata.automapping.model.dto.AddGameDto;
import bg.softuni.springdata.automapping.model.dto.EditGameDto;
import bg.softuni.springdata.automapping.model.dto.GameTitleAndPriceViewDto;
import bg.softuni.springdata.automapping.model.dto.GameViewDto;
import bg.softuni.springdata.automapping.model.entity.Game;
import bg.softuni.springdata.automapping.model.entity.Role;
import bg.softuni.springdata.automapping.repository.GameRepository;
import bg.softuni.springdata.automapping.service.GameService;
import bg.softuni.springdata.automapping.service.UserService;
import bg.softuni.springdata.automapping.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public GameServiceImpl(GameRepository gameRepository, UserService userService, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.gameRepository = gameRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public String addGame(AddGameDto addGameDto) {
        StringBuilder sb = new StringBuilder();

        if (this.userService.getLoggedInUser() == null) {
            sb.append("There is no logged in user");
        } else if (this.userService.getLoggedInUser().getRole() != Role.ADMIN) {
            sb.append("Only an admin user can add new games");

        } else {
            String validationResult = this.validateGame(addGameDto);
            if (!validationResult.isEmpty()) {
                sb.append(validationResult);
            } else {
                Game game = this.modelMapper.map(addGameDto, Game.class);
                this.gameRepository.save(game);
                sb.append(String.format("Added %s", game.getTitle()));
            }
        }

        return sb.toString().trim();
    }

    @Override
    public String editGame(EditGameDto editGameDto) {
        StringBuilder sb = new StringBuilder();

        if (this.userService.getLoggedInUser() == null) {
            sb.append("There is no logged in user");
        } else if (this.userService.getLoggedInUser().getRole() != Role.ADMIN) {
            sb.append("Only an admin user can edit games");

        } else {
            Game oldGame = this.getValidGame(editGameDto.getId());

            if (oldGame == null) {
                sb.append(String.format("No game with ID %d found", editGameDto.getId()));

            } else {
                AddGameDto addGameDto = this.modelMapper.map(oldGame, AddGameDto.class);
                setNewValuesToAddGameDto(editGameDto, addGameDto);

                String validationResult = this.validateGame(addGameDto);
                if (!validationResult.isEmpty()) {
                    sb.append(validationResult);
                } else {
                    String title = oldGame.getTitle();
                    setNewValueToGame(editGameDto, oldGame);
                    this.gameRepository.save(oldGame);
                    sb.append(String.format("Edited %s", title));
                }
            }
        }

        return sb.toString().trim();
    }

    @Override
    public String deleteGame(Long id) {
        StringBuilder sb = new StringBuilder();

        if (this.userService.getLoggedInUser() == null) {
            sb.append("There is no logged in user");
        } else if (this.userService.getLoggedInUser().getRole() != Role.ADMIN) {
            sb.append("Only an admin user can delete games");

        } else {
            Game toDelete = this.getValidGame(id);

            if (toDelete == null) {
                sb.append(String.format("No game with ID %d found", id));
            } else {
                sb.append(String.format("Deleted %s", toDelete.getTitle()));
                this.gameRepository.delete(toDelete);
            }
        }

        return sb.toString().trim();
    }

    @Override
    public List<GameTitleAndPriceViewDto> findTitlesAndPriceOfAllGames() {
        return this.gameRepository
                .findAll()
                .stream()
                .map(game -> this.modelMapper.map(game, GameTitleAndPriceViewDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public GameViewDto findGame(String gameTitle) {
        Game game = this.gameRepository
                .findByTitle(gameTitle);

        return this.modelMapper.map(game, GameViewDto.class);
    }

    @Override
    public Game findByTitleAndUserIsNull(String gameTitle) {
        return this.gameRepository
                .findByTitleAndUserIsNull(gameTitle);
    }

    @Override
    public String purchaseGame(Game game) {
        StringBuilder sb = new StringBuilder();

        game.setUser(this.userService.getLoggedInUser());
        this.gameRepository.save(game);
        this.userService.getLoggedInUser().getGames().add(game);

        sb.append(String.format("User %s purchased game %s",
                this.userService.getLoggedInUser().getFullName(),
                game.getTitle()));


        return sb.toString().trim();
    }

    @Override
    public void save(Game game) {
        this.gameRepository
                .save(game);
    }


    private String validateGame(AddGameDto addGameDto) {
        StringBuilder sb = new StringBuilder();

        this.validationUtil.violations(addGameDto)
                .forEach(v -> sb.append(
                        v.getMessage()).append(System.lineSeparator())
                );

        return sb.toString().trim();
    }

    private Game getValidGame(Long id) {
        return this.gameRepository
                .findById(id)
                .orElse(null);
    }

    private void setNewValuesToAddGameDto(EditGameDto editGameDto, AddGameDto addGameDto) {
        Arrays.stream(editGameDto.getValues())
                .forEach(value -> {
                    String[] tokens = value.split("=");

                    switch (tokens[0]) {
                        case "title" -> addGameDto.setTitle(tokens[1]);
                        case "price" -> addGameDto.setPrice(new BigDecimal(tokens[1]));
                        case "size" -> addGameDto.setSize(Double.parseDouble(tokens[1]));
                        case "trailer" -> addGameDto.setTrailer(tokens[1]);
                        case "thumbnailUrl" -> addGameDto.setThumbnailUrl(tokens[1]);
                        case "description" -> addGameDto.setDescription(tokens[1]);
                        case "releaseDate" -> addGameDto.setReleaseDate(tokens[1]);
                    }
                });
    }

    private void setNewValueToGame(EditGameDto editGameDto, Game game) {
        Arrays.stream(editGameDto.getValues())
                .forEach(value -> {
                    String[] tokens = value.split("=");

                    switch (tokens[0]) {
                        case "title" -> game.setTitle(tokens[1]);
                        case "price" -> game.setPrice(new BigDecimal(tokens[1]));
                        case "size" -> game.setSize(Double.parseDouble(tokens[1]));
                        case "trailer" -> game.setTrailer(tokens[1]);
                        case "thumbnailUrl" -> game.setImageThumbnail(tokens[1]);
                        case "description" -> game.setDescription(tokens[1]);
                        case "releaseDate" -> game.setReleaseDate(
                                LocalDate.parse(tokens[1], DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                    }
                });
    }
}
