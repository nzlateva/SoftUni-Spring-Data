package bg.softuni.springdata.automapping;

import bg.softuni.springdata.automapping.model.dto.*;
import bg.softuni.springdata.automapping.service.GameService;
import bg.softuni.springdata.automapping.service.OrderService;
import bg.softuni.springdata.automapping.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final UserService userService;
    private final GameService gameService;
    private final OrderService orderService;
    private final BufferedReader reader;

    public CommandLineRunnerImpl(UserService userService, GameService gameService, OrderService orderService) {
        this.userService = userService;
        this.gameService = gameService;
        this.orderService = orderService;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run(String... args) throws Exception {

        while (true) {
            System.out.println("Enter command:");
            String[] tokens = this.reader.readLine().split("\\|");

            switch (tokens[0]) {
                case "RegisterUser" -> System.out.println(this.userService
                        .register(new UserRegisterDto(tokens[1], tokens[2], tokens[3], tokens[4])));
                case "LoginUser" -> System.out.println(this.userService
                        .login(new UserLoginDto(tokens[1], tokens[2])));
                case "Logout" -> System.out.println(this.userService
                        .logout());
                case "AddGame" -> System.out.println(this.gameService
                        .addGame(new AddGameDto(
                                tokens[1],
                                new BigDecimal(tokens[2]),
                                Double.parseDouble(tokens[3]),
                                tokens[4], tokens[5], tokens[6], tokens[7])
                        ));
                case "EditGame" -> System.out.println(this.gameService
                        .editGame(new EditGameDto(
                                Long.parseLong(tokens[1]),
                                Arrays.stream(tokens)
                                        .skip(2)
                                        .collect(Collectors.toList())
                                        .toArray(String[]::new))
                        ));
                case "DeleteGame" -> System.out.println(this.gameService
                        .deleteGame(Long.parseLong(tokens[1])));
                case "AllGames" -> printTitlesAndPriceOfAllGames();
                case "DetailGame" -> printGameDetails(tokens[1]);
                case "OwnedGames" -> printUserOwnedGames();
                case "AddItem" -> System.out.println(this.orderService
                        .addToCard(tokens[1]));
                case "RemoveItem" -> System.out.println(this.orderService
                        .removeFromCard(tokens[1]));
                case "BuyItem" -> System.out.println(this.orderService
                        .buyAllGames());
            }
        }

    }

    private void printTitlesAndPriceOfAllGames() {
        this.gameService
                .findTitlesAndPriceOfAllGames()
                .forEach(gameTitleAndPriceDto ->
                        System.out.printf("%s %s%n",
                                gameTitleAndPriceDto.getTitle(),
                                gameTitleAndPriceDto.getPrice()));
    }

    private void printGameDetails(String gameTitle) {
        GameViewDto game = this.gameService
                .findGame(gameTitle);

        System.out.println(game);
    }

    private void printUserOwnedGames() {
        this.userService
                .getUserOwnedGames()
                .forEach(ownedGameViewDto ->
                        System.out.println(ownedGameViewDto.getTitle()));
    }
}
