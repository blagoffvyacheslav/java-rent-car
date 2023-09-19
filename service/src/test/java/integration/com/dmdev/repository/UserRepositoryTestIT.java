package integration.com.dmdev.repository;

import com.dmdev.dto.UserFilterDto;
import com.dmdev.entity.User;
import com.dmdev.entity.Role;
import com.dmdev.repository.UserRepository;
import com.dmdev.utils.QPredicate;
import com.dmdev.utils.UserPredicate;
import com.querydsl.core.types.Predicate;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.entity.UserDetailsTestIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static com.dmdev.entity.QUser.user;
import integration.com.dmdev.entity.UserTestIT;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserRepositoryTestIT extends IntegrationBaseTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveUserWithoutUserDetails() {
        var userToSave = UserTestIT.createUser();

        var savedUser = userRepository.save(userToSave);

        assertThat(savedUser).isNotNull();
    }

    @Test
    void shouldSaveUserWithUserDetails() {
        var userToSave = UserTestIT.createUser();
        var userDetails = UserDetailsTestIT.createUserDetails();
        userDetails.setUser(userToSave);

        var savedUser = userRepository.save(userToSave);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(userDetails).isNotNull();
    }

    @Test
    void shouldFindByIdUser() {
        var expectedUser = Optional.of(UserTestIT.getExistUser());

        var actualUser = userRepository.findById(UserTestIT.TEST_EXISTS_USER_ID);

        assertThat(actualUser).isNotNull();
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void shouldUpdateUser() {
        var userToUpdate = userRepository.findById(UserTestIT.TEST_EXISTS_USER_ID).get();
        var userDetails = userToUpdate.getUserDetails();
        userToUpdate.setPassword("8967562");
        userDetails.setUser(userToUpdate);

        userRepository.save(userToUpdate);

        var updatedUser = userRepository.findById(userToUpdate.getId()).get();

        assertThat(updatedUser).isEqualTo(userToUpdate);
    }

    @Test
    void shouldDeleteUser() {
        var user = userRepository.findById(UserTestIT.TEST_USER_ID_FOR_DELETE);

        user.ifPresent(u -> userRepository.delete(u));

        assertThat(userRepository.findById(UserTestIT.TEST_USER_ID_FOR_DELETE)).isEmpty();
    }

    @Test
    void shouldFindAllUsers() {
        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(2);

        List<String> emails = users.stream().map(User::getEmail).collect(toList());
        assertThat(emails).containsExactlyInAnyOrder("admin@gmail.com", "client@client.com");
    }


    @Test
    void shouldReturnUserByEmailAndPasswordWithFilter() {
        var userFilterDto = UserFilterDto.builder()
                .email("client@client.com")
                .build();

        var optionalUser = userRepository.findOne(UserPredicate.build(userFilterDto));

        assertThat(optionalUser).isNotNull();
        optionalUser.ifPresent(user -> assertThat(user.getId()).isEqualTo(UserTestIT.getExistUser().getId()));
        assertThat(optionalUser).isEqualTo(Optional.of(UserTestIT.getExistUser()));
    }

    @Test
    void shouldReturnUserByEmailAndPassword() {
        var optionalUser = userRepository.findByEmailAndPassword("client@client.com", "qwerty");

        assertThat(optionalUser).isNotNull();
        optionalUser.ifPresent(user -> assertThat(user.getId()).isEqualTo(UserTestIT.getExistUser().getId()));
        assertThat(optionalUser).isEqualTo(Optional.of(UserTestIT.getExistUser()));
    }

    @Test
    void shouldReturnUserByEmail() {
        var optionalUser = userRepository.findByEmail("client@client.com");

        assertThat(optionalUser).isNotNull();
        optionalUser.ifPresent(user -> assertThat(user.getId()).isEqualTo(UserTestIT.getExistUser().getId()));
        assertThat(optionalUser).isEqualTo(Optional.of(UserTestIT.getExistUser()));
    }

    @Test
    void shouldReturnUserByPhone() {
        var optionalUser = userRepository.findByPhone("+1 720 123 45 67");

        assertThat(optionalUser).isNotEmpty();
        optionalUser.ifPresent(user -> assertEquals(user, UserTestIT.getExistUser()));
    }

    @Test
    void shouldReturnUsersWithOrders() {
        List<User> users = userRepository.findAllWithOrders();

        assertThat(users).isNotEmpty().hasSize(2);
        List<String> emails = users.stream().map(User::getEmail).collect(toList());
        assertThat(emails).containsExactlyInAnyOrder("admin@gmail.com", "client@client.com");
    }

    @Test
    void shouldReturnUsersWithoutOrders() {
        List<User> users = userRepository.findAllWithoutOrders();

        assertThat(users).isEmpty();
    }

    @Test
    void shouldReturnUserByRole() {
        var users = userRepository.findAllByRole(Role.CLIENT);

        assertThat(users).hasSize(1);

        List<String> emails = users.stream().map(User::getEmail).collect(toList());
        assertThat(emails).containsExactlyInAnyOrder("client@client.com");
    }

    @Test
    void shouldReturnUsersByRegistrationDate() {
        var users = userRepository.findAllByRegistrationDate(LocalDate.of(2023, 7, 3));

        assertThat(users).hasSize(2);

        List<String> emails = users.stream().map(User::getEmail).collect(toList());
        assertThat(emails).containsExactlyInAnyOrder("admin@gmail.com", "client@client.com");
    }

    @Test
    void shouldReturnUserByUserFilterWithBirthday() {
        var userFilterDto = UserFilterDto.builder()
                .birthday(LocalDate.of(1995, 7, 22))
                .build();

        Iterable<User> users = userRepository.findAll(UserPredicate.build(userFilterDto));

        assertThat(users).hasSize(1);
        assertThat(users.iterator().next().getUserDetails().getName()).isEqualTo("Semen");
        assertThat(users.iterator().next().getUserDetails().getLastname()).isEqualTo("Kobelev");
    }


    @Test
    void shouldReturnUsersByUserFilterWithNameOrSurnameAndBirthdayOrderedByEmail() {
        var userFilterDto = UserFilterDto.builder()
                .name("Semen")
                .lastname("Kobelev")
                .birthday(LocalDate.of(1995, 7, 22))
                .build();

        Predicate orPredicates = QPredicate.builder()
                .add(userFilterDto.getName(), user.userDetails.name::eq)
                .add(userFilterDto.getLastname(), user.userDetails.lastname::eq)
                .buildOr();

        Predicate birthdayPredicates = QPredicate.builder()
                .add(userFilterDto.getBirthday(), user.userDetails.birthday::eq)
                .buildAnd();

        Predicate resultPredicates = QPredicate.builder()
                .addPredicate(orPredicates)
                .addPredicate(birthdayPredicates)
                .buildAnd();

        Sort sort = Sort.by("email").descending();
        Iterable<User> users = userRepository.findAll(resultPredicates, sort);

        assertThat(users).hasSize(1);
        List<String> emails = StreamSupport.stream(users.spliterator(), false).map(User::getEmail).collect(toList());
        assertThat(emails).contains("admin@gmail.com");
    }
}