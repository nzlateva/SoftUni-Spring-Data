package bg.softuni.springdata.automapping.service;

public interface OrderService {

    String addToCard(String gameTitle);

    String removeFromCard(String gameTitle);

    String buyAllGames();
}
