package bg.softuni.springdata.automapping.service.impl;

import bg.softuni.springdata.automapping.model.dto.GameViewDto;
import bg.softuni.springdata.automapping.model.dto.OwnedGameViewDto;
import bg.softuni.springdata.automapping.model.dto.UserLoginDto;
import bg.softuni.springdata.automapping.model.dto.UserRegisterDto;
import bg.softuni.springdata.automapping.model.entity.Game;
import bg.softuni.springdata.automapping.model.entity.Role;
import bg.softuni.springdata.automapping.model.entity.User;
import bg.softuni.springdata.automapping.repository.UserRepository;
import bg.softuni.springdata.automapping.service.GameService;
import bg.softuni.springdata.automapping.service.UserService;
import bg.softuni.springdata.automapping.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private User loggedInUser;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public User getLoggedInUser() {
        return loggedInUser;
    }

    @Override
    public List<OwnedGameViewDto> getUserOwnedGames() {

        return this.loggedInUser
                .getGames()
                .stream()
                .map(game -> this.modelMapper.map(game, OwnedGameViewDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public String register(UserRegisterDto userRegisterDto) {
        StringBuilder sb = new StringBuilder();

        if (this.userRepository
                .findUserByEmail(userRegisterDto.getEmail()) != null) {

            sb.append("User with this email address is already registered");

        } else if (!userRegisterDto.getPassword().equals(userRegisterDto.getConfirmPassword())) {
            sb.append("Wrong confirm password");

        } else {

            Set<ConstraintViolation<UserRegisterDto>> violations =
                    this.validationUtil.violations(userRegisterDto);

            if (!violations.isEmpty()) {
                violations
                        .stream()
                        .map(ConstraintViolation::getMessage)
                        .forEach(m -> sb.append(m).append(System.lineSeparator()));
            } else {
                User user = this.modelMapper.map(userRegisterDto, User.class);

                if (this.userRepository.count() == 0) {
                    user.setRole(Role.ADMIN);
                } else {
                    user.setRole(Role.USER);
                }

                this.userRepository.save(user);

                sb.append(String.format("%s was registered",
                        user.getFullName()));
            }
        }

        return sb.toString().trim();
    }

    @Override
    public String login(UserLoginDto userLoginDto) {
        StringBuilder sb = new StringBuilder();

        if (this.loggedInUser != null) {
            sb.append("There is another user currently logged in");

        } else {
            User user = this.userRepository
                    .findByEmailAndPassword(
                            userLoginDto.getEmail(), userLoginDto.getPassword());

            if (user == null) {
                sb.append("Incorrect username / password");
            } else {
                this.loggedInUser = user;
                sb.append("Successfully logged in ").append(user.getFullName());
            }
        }

        return sb.toString().trim();
    }


    @Override
    public String logout() {
        StringBuilder sb = new StringBuilder();

        if (this.loggedInUser == null) {
            sb.append("Cannot log out. No user was logged in.");
        } else {
            sb.append(String.format("User %s successfully logged out",
                    this.loggedInUser.getFullName()));

            this.loggedInUser = null;
        }

        return sb.toString().trim();
    }
}
