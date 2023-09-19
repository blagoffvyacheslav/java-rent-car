package integration.com.dmdev.repository;

import com.dmdev.dto.UserDetailsFilterDto;
import com.dmdev.entity.User;
import com.dmdev.entity.UserDetails;
import com.dmdev.repository.UserDetailsRepository;
import com.dmdev.repository.UserRepository;
import com.dmdev.utils.UserDetailsPredicate;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.entity.UserDetailsTestIT;
import integration.com.dmdev.entity.UserTestIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserDetailsRepositoryTestIT extends IntegrationBaseTest {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldFindById() {
        var expectedUserDetails = Optional.of(UserDetailsTestIT.getExistUserDetails());

        var actualUserDetails = userDetailsRepository.findById(UserDetailsTestIT.TEST_EXISTS_USER_DETAILS_ID);

        assertThat(actualUserDetails).isNotNull();
        assertEquals(expectedUserDetails, actualUserDetails);
    }

    @Test
    void shouldUpdateUserDetails() {
        var userDetailsToUpdate = userDetailsRepository.findById(UserDetailsTestIT.TEST_EXISTS_USER_DETAILS_ID).get();

        userDetailsRepository.save(userDetailsToUpdate);

        var updatedUserDetails = userDetailsRepository.findById(userDetailsToUpdate.getId()).get();
        var updatedUser = userRepository.findById(userDetailsToUpdate.getUser().getId()).get();

        assertThat(updatedUserDetails).isEqualTo(userDetailsToUpdate);
        assertThat(updatedUser.getUserDetails()).isEqualTo(updatedUserDetails);
    }

    @Test
    void shouldDeleteUserDetails() {
        var userDetailsToDelete = userDetailsRepository.findById(UserDetailsTestIT.TEST_USER_DETAILS_ID_FOR_DELETE);

        userDetailsToDelete.ifPresent(u -> userDetailsRepository.delete(u));

        assertThat(userDetailsRepository.findById(UserDetailsTestIT.TEST_USER_DETAILS_ID_FOR_DELETE)).isEmpty();
    }

    @Test
    void shouldFindAllUserDetails() {
        List<UserDetails> userDetails = userDetailsRepository.findAll();
        assertThat(userDetails).hasSize(2);

        List<String> names = userDetails.stream().map(UserDetails::getName).collect(toList());
        assertThat(names).containsExactlyInAnyOrder("Semen", "Vyacheslav");
    }


    @Test
    void shouldReturnUserDetailsByUserId() {
        var optionalUserDetails = userDetailsRepository.findByUserId(UserTestIT.TEST_EXISTS_USER_ID);

        assertThat(optionalUserDetails).isNotNull();
        assertThat(optionalUserDetails.getId()).isEqualTo(UserDetailsTestIT.getExistUserDetails().getId());
        assertThat(optionalUserDetails).isEqualTo(UserDetailsTestIT.getExistUserDetails());
    }

    @Test
    void shouldReturnUserDetailsByNameAndSurnameWithFilter() {
        var userDetailsFilter = UserDetailsFilterDto.builder()
                .name("Vyacheslav")
                .lastname("Blagov")
                .build();

        Iterable<UserDetails> userDetails = userDetailsRepository.findAll(UserDetailsPredicate.build(userDetailsFilter));

        assertThat(userDetails).hasSize(1);
        assertThat(userDetails.iterator().next()).isEqualTo(UserDetailsTestIT.getExistUserDetails());
    }

    @Test
    void shouldReturnUserDetailsByNameAndSurnameWithoutFilter() {
        List<UserDetails> userDetails = userDetailsRepository.findAllByNameContainingIgnoreCaseAndLastnameContainingIgnoreCase("Vyacheslav", "Blagov");

        assertThat(userDetails).hasSize(1);
        assertThat(userDetails.get(0)).isEqualTo(UserDetailsTestIT.getExistUserDetails());
    }

    @Test
    void shouldReturnUserDetailsByBirthdayOrderedBySurnameAndName() {
        var userDetailsFilter = UserDetailsFilterDto.builder()
                .birthday(LocalDate.of(1994, 12, 5))
                .build();
        Sort sort = Sort.by("lastname").descending().by("name").descending();
        Iterable<UserDetails> userDetails = userDetailsRepository.findAll(UserDetailsPredicate.build(userDetailsFilter), sort);
        assertThat(userDetails).hasSize(1);

        List<String> emails = StreamSupport.stream(userDetails.spliterator(), false).map(UserDetails::getUser).map(User::getEmail).collect(toList());
        assertThat(emails).contains("client@client.com");
    }

    @Test
    void shouldNotReturnUserDetailsByRegistrationDate() {
        List<UserDetails> userDetails = userDetailsRepository.findByRegistrationDate(LocalDate.of(2022, 9, 21));

        assertThat(userDetails).isEmpty();
    }

    @Test
    void shouldReturnUserDetailsByRegistrationDate() {
        List<UserDetails> userDetails = userDetailsRepository.findByRegistrationDate(LocalDate.of(2023, 7, 3));

        assertThat(userDetails).hasSize(2);
    }

    @Test
    void shouldReturnUserDetailsByRegistrationDates() {
        List<UserDetails> userDetails = userDetailsRepository.findByRegistrationDateBetween(LocalDate.of(2022, 1, 1), LocalDate.of(2023, 12, 31));

        assertThat(userDetails).hasSize(2);
    }
}