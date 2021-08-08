package bg.softuni.springdata.automapping.service;

import bg.softuni.springdata.automapping.model.dto.OwnedGameViewDto;
import bg.softuni.springdata.automapping.model.dto.UserLoginDto;
import bg.softuni.springdata.automapping.model.dto.UserRegisterDto;
import bg.softuni.springdata.automapping.model.entity.User;

import java.util.List;

public interface UserService {

    String register(UserRegisterDto userRegisterDto);

    String login(UserLoginDto userLoginDto);

    String logout();

    User getLoggedInUser();

    List<OwnedGameViewDto> getUserOwnedGames();
}
