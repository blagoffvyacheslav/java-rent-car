package integration.com.dmdev.service;

import com.dmdev.dto.UserDetailsFilterDto;
import com.dmdev.dto.UserDetailsReadDto;
import com.dmdev.dto.UserDetailsUpdateDto;
import com.dmdev.service.UserDetailsService;
import com.dmdev.service.UserService;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.dto.TestDto;
import integration.com.dmdev.entity.UserDetailsTestIT;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
class UserDetailsServiceTestIT extends IntegrationBaseTest {

    private final UserDetailsService userDetailsService;
    private final UserService userService;

    @Test
    void shouldSaveUserDetailsCorrectly() {
        var userCreateDTO = TestDto.createUserCreateDTO();

        var actualUser = userService.create(userCreateDTO);

        assertTrue(actualUser.isPresent());
        assertEquals(userCreateDTO.getName(), actualUser.get().getUserDetailsDto().getName());
        assertEquals(userCreateDTO.getLastname(), actualUser.get().getUserDetailsDto().getLastname());
        assertEquals(userCreateDTO.getAddress(), actualUser.get().getUserDetailsDto().getAddress());
        assertEquals(userCreateDTO.getPhone(), actualUser.get().getUserDetailsDto().getPhone());
    }

    @Test
    void shouldFindAllUserDetails() {
        var userDetails = userDetailsService.getAll(UserDetailsFilterDto.builder().build(), 0, 4);

        assertThat(userDetails.getContent()).hasSize(2);
        assertThat(userDetails.getTotalElements()).isEqualTo(2L);
        assertThat(userDetails.getNumberOfElements()).isEqualTo(2L);

        var addresses = userDetails.getContent().stream().map(UserDetailsReadDto::getAddress).collect(toList());
        assertThat(addresses).containsExactlyInAnyOrder("17 Lenin st", "21 Lebedyanskya st");
    }

    @Test
    void shouldReturnUserDetailsByFilter() {
        var userCreateDto = TestDto.createUserCreateDTO();
        var userReadDto = userService.create(userCreateDto);

        var userDetailsFilter = UserDetailsFilterDto.builder()
                .address(userCreateDto.getAddress())
                .build();

        var userDetails = userDetailsService.getAll(userDetailsFilter, 0, 4);

        assertThat(userDetails.getContent()).hasSize(2);
        assertThat(userDetails.getTotalElements()).isEqualTo(2L);
        assertThat(userDetails.getNumberOfElements()).isEqualTo(2);
        assertThat(userDetails.getContent().get(0).getAddress()).isEqualTo(userReadDto.get().getUserDetailsDto().getAddress());
    }

    @Test
    void shouldReturnUserDetailsByNameAndLastname() {
        var userCreateDto = TestDto.createUserCreateDTO();
        var userDetailsDto = userService.create(userCreateDto).get().getUserDetailsDto();

        var userDetails = userDetailsService.getAllByNameAndLastname("Semen", "Kobelev");

        assertThat(userDetails).hasSize(1);
        assertThat(userDetails.get(0).getAddress()).isEqualTo(userDetailsDto.getAddress());
    }

    @Test
    void shouldReturnUserDetailsById() {
        var userCreateDto = TestDto.createUserCreateDTO();
        var expectedUserDetails = userService.create(userCreateDto).get().getUserDetailsDto();

        var actualUserDetails = userDetailsService.getById(expectedUserDetails.getId());

        assertThat(actualUserDetails).isNotNull();
        assertEquals(expectedUserDetails, actualUserDetails.get());
    }

    @Test
    void shouldUpdateUserDetailsCorrectly() {
        var userCreateDto = TestDto.createUserCreateDTO();
        var userDetailsUpdateDto = new UserDetailsUpdateDto(
                "test",
                "test",
                "Moscow",
                "1111111111");

        var savedUserDetails = userService.create(userCreateDto).get().getUserDetailsDto();

        var actualUserDetails = userDetailsService.update(savedUserDetails.getId(), userDetailsUpdateDto);

        assertThat(actualUserDetails).isNotNull();
        actualUserDetails.ifPresent(userDetail -> {
            assertEquals(userDetailsUpdateDto.getName(), userDetail.getName());
            assertEquals(userDetailsUpdateDto.getLastname(), userDetail.getLastname());
            assertSame(userDetailsUpdateDto.getAddress(), userDetail.getAddress());
            assertSame(userDetailsUpdateDto.getPhone(), userDetail.getPhone());
        });
    }

    @Test
    void shouldDeleteUserDetailByIdCorrectly() {
        assertTrue(userDetailsService.deleteById(UserDetailsTestIT.TEST_USER_DETAILS_ID_FOR_DELETE));
    }

    @Test
    void shouldNotDeleteUserWithNonExistsId() {
        assertFalse(userDetailsService.deleteById(999999L));
    }
}