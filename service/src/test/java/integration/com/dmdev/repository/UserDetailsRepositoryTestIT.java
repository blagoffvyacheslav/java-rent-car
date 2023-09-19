package integration.com.dmdev.repository;

import com.dmdev.dto.UserDetailsFilterDto;
import com.dmdev.repository.UserDetailsRepository;
import com.dmdev.repository.UserRepository;
import com.dmdev.utils.predicate.UserDetailsPredicateBuilder;
import integration.com.dmdev.IntegrationBaseTest;
import utils.builder.UserDetailsBuilder;
import utils.builder.UserBuilder;

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

    @Autowired
    private UserDetailsPredicateBuilder userDetailsPredicateBuilder;

    @Test
    void shouldFindById() {
        var expectedUserDetails = Optional.of(UserDetailsBuilder.getExistUserDetails());

        var actualUserDetails = userDetailsRepository.findById(UserDetailsBuilder.TEST_EXISTS_USER_DETAILS_ID);

        assertThat(actualUserDetails).isNotNull();
        assertEquals(expectedUserDetails, actualUserDetails);
    }

    @Test
    void shouldUpdateUserDetails() {
        var userDetailsToUpdate = userDetailsRepository.findById(UserDetailsBuilder.TEST_EXISTS_USER_DETAILS_ID).get();

        userDetailsRepository.save(userDetailsToUpdate);

        var updatedUserDetails = userDetailsRepository.findById(userDetailsToUpdate.getId()).get();
        var updatedUser = userRepository.findById(userDetailsToUpdate.getUser().getId()).get();

        assertThat(updatedUserDetails).isEqualTo(userDetailsToUpdate);
        assertThat(updatedUser.getUserDetails()).isEqualTo(updatedUserDetails);
    }

    @Test
    void shouldDeleteUserDetails() {
        var userDetailsToDelete = userDetailsRepository.findById(UserDetailsBuilder.TEST_USER_DETAILS_ID_FOR_DELETE);

        userDetailsToDelete.ifPresent(u -> userDetailsRepository.delete(u));

        assertThat(userDetailsRepository.findById(UserDetailsBuilder.TEST_USER_DETAILS_ID_FOR_DELETE)).isEmpty();
    }

    @Test
    void shouldFindAllUserDetails() {
        List<com.dmdev.entity.UserDetails> userDetails = userDetailsRepository.findAll();
        assertThat(userDetails).hasSize(2);

        List<String> names = userDetails.stream().map(com.dmdev.entity.UserDetails::getName).collect(toList());
        assertThat(names).containsExactlyInAnyOrder("Semen", "Vyacheslav");
    }


    @Test
    void shouldReturnUserDetailsByUserId() {
        var optionalUserDetails = userDetailsRepository.findByUserId(UserBuilder.TEST_EXISTS_USER_ID);

        assertThat(optionalUserDetails).isNotNull();
        assertThat(optionalUserDetails.getId()).isEqualTo(UserDetailsBuilder.getExistUserDetails().getId());
        assertThat(optionalUserDetails).isEqualTo(UserDetailsBuilder.getExistUserDetails());
    }

    @Test
    void shouldReturnUserDetailsByNameAndSurnameWithFilter() {
        var userDetailsFilter = UserDetailsFilterDto.builder()
                .name("Vyacheslav")
                .lastname("Blagov")
                .build();

        Iterable<com.dmdev.entity.UserDetails> userDetails = userDetailsRepository.findAll(userDetailsPredicateBuilder.build(userDetailsFilter));

        assertThat(userDetails).hasSize(1);
        assertThat(userDetails.iterator().next()).isEqualTo(UserDetailsBuilder.getExistUserDetails());
    }

    @Test
    void shouldReturnUserDetailsByNameAndSurnameWithoutFilter() {
        List<com.dmdev.entity.UserDetails> userDetails = userDetailsRepository.findAllByNameContainingIgnoreCaseAndLastnameContainingIgnoreCase("Vyacheslav", "Blagov");

        assertThat(userDetails).hasSize(1);
        assertThat(userDetails.get(0)).isEqualTo(UserDetailsBuilder.getExistUserDetails());
    }

    @Test
    void shouldReturnUserDetailsByBirthdayOrderedBySurnameAndName() {
        var userDetailsFilter = UserDetailsFilterDto.builder()
                .birthday(LocalDate.of(1994, 12, 5))
                .build();
        Sort sort = Sort.by("lastname").descending().by("name").descending();
        Iterable<com.dmdev.entity.UserDetails> userDetails = userDetailsRepository.findAll(userDetailsPredicateBuilder.build(userDetailsFilter), sort);
        assertThat(userDetails).hasSize(1);

        List<String> emails = StreamSupport.stream(userDetails.spliterator(), false).map(com.dmdev.entity.UserDetails::getUser).map(com.dmdev.entity.User::getEmail).collect(toList());
        assertThat(emails).contains("client@client.com");
    }

    @Test
    void shouldNotReturnUserDetailsByRegistrationDate() {
        List<com.dmdev.entity.UserDetails> userDetails = userDetailsRepository.findByRegistrationDate(LocalDate.of(2022, 9, 21));

        assertThat(userDetails).isEmpty();
    }

    @Test
    void shouldReturnUserDetailsByRegistrationDate() {
        List<com.dmdev.entity.UserDetails> userDetails = userDetailsRepository.findByRegistrationDate(LocalDate.of(2023, 7, 3));

        assertThat(userDetails).hasSize(2);
    }

    @Test
    void shouldReturnUserDetailsByRegistrationDates() {
        List<com.dmdev.entity.UserDetails> userDetails = userDetailsRepository.findByRegistrationDateBetween(LocalDate.of(2022, 1, 1), LocalDate.of(2023, 12, 31));

        assertThat(userDetails).hasSize(2);
    }
}