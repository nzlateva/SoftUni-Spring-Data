package bg.softuni.springdata.automapping.service.impl;

import bg.softuni.springdata.automapping.model.entity.Game;
import bg.softuni.springdata.automapping.model.entity.Order;
import bg.softuni.springdata.automapping.repository.OrderRepository;
import bg.softuni.springdata.automapping.service.GameService;
import bg.softuni.springdata.automapping.service.OrderService;
import bg.softuni.springdata.automapping.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final GameService gameService;
    private Order currentOrder;

    public OrderServiceImpl(OrderRepository orderRepository, UserService userService, GameService gameService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.gameService = gameService;
    }

    @Override
    public String addToCard(String gameTitle) {
        StringBuilder sb = new StringBuilder();

        if (this.userService.getLoggedInUser() == null) {
            sb.append("There is currently no logged in user");

        } else {
            Game toAdd = this.gameService
                    .findByTitleAndUserIsNull(gameTitle);

            if (toAdd == null) {
                sb.append(String.format("%s is not available for purchase", gameTitle));

            } else {
                if (this.currentOrder == null) {
                    this.currentOrder = new Order();
                    this.currentOrder.setBuyer(this.userService.getLoggedInUser());
                    this.orderRepository.save(this.currentOrder);
                }

                this.currentOrder.getGames().add(toAdd);
                toAdd.setOrder(this.currentOrder);
                this.gameService.save(toAdd);
                sb.append(String.format("%s added to the shopping card", gameTitle));
            }
        }

        return sb.toString().trim();
    }

    @Override
    public String removeFromCard(String gameTitle) {
        StringBuilder sb = new StringBuilder();

        if (this.userService.getLoggedInUser() == null) {
            sb.append("There is currently no logged in user");

        } else if (this.currentOrder == null) {
            sb.append("There is currently no active order. Please start an order by adding a game to your shopping card");

        } else {
            Game toRemove = this.currentOrder.getGames()
                    .stream()
                    .filter(g -> g.getTitle().equals(gameTitle))
                    .findFirst()
                    .orElse(null);

            if (toRemove == null) {
                sb.append(
                        String.format("%s is currently not present in the shopping card",
                                gameTitle));
            } else {
                this.currentOrder.getGames().remove(toRemove);
                toRemove.setOrder(null);
                this.gameService.save(toRemove);
                sb.append(String.format("%s removed from the shopping card", gameTitle));
            }
        }

        return sb.toString().trim();
    }

    @Override
    public String buyAllGames() {
        StringBuilder sb = new StringBuilder();

        if (this.userService.getLoggedInUser() == null) {
            sb.append("There is currently no logged in user");

        } else if (this.currentOrder == null) {
            sb.append("There is currently no active order. Please start an order by adding a game to your shopping card");

        } else if (this.currentOrder.getGames().size() == 0) {
            sb.append("No games added to shopping card");

        } else {
            this.currentOrder.getGames()
                    .forEach(game ->
                            sb.append(this.gameService.purchaseGame(game))
                                    .append(System.lineSeparator())
                    );

            this.currentOrder = null;
        }

        return sb.toString().trim();
    }
}
