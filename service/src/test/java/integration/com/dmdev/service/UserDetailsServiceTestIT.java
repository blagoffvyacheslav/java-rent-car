package integration.com.dmdev.service;

import com.dmdev.dto.UserDetailsFilterDto;
import com.dmdev.dto.UserDetailsReadDto;
import com.dmdev.dto.UserDetailsUpdateDto;
import com.dmdev.service.UserDetailsService;
import com.dmdev.service.UserService;
import integration.com.dmdev.IntegrationBaseTest;
import utils.builder.TestDtoBuilder;
import utils.builder.UserDetailsBuilder;
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
        var userCreateDTO = TestDtoBuilder.createUserCreateDTO();

        var actualUser = userService.create(userCreateDTO);

        assertEquals(userCreateDTO.getName(), actualUser.getUserDetailsDto().getName());
        assertEquals(userCreateDTO.getLastname(), actualUser.getUserDetailsDto().getLastname());
        assertEquals(userCreateDTO.getAddress(), actualUser.getUserDetailsDto().getAddress());
        assertEquals(userCreateDTO.getPhone(), actualUser.getUserDetailsDto().getPhone());
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
        var userCreateDto = TestDtoBuilder.createUserCreateDTO();
        var userReadDto = userService.create(userCreateDto);

        var userDetailsFilter = UserDetailsFilterDto.builder()
                .address(userCreateDto.getAddress())
                .build();

        var userDetails = userDetailsService.getAll(userDetailsFilter, 0, 4);

        assertThat(userDetails.getContent()).hasSize(2);
        assertThat(userDetails.getTotalElements()).isEqualTo(2L);
        assertThat(userDetails.getNumberOfElements()).isEqualTo(2);
        assertThat(userDetails.getContent().get(0).getAddress()).isEqualTo(userReadDto.getUserDetailsDto().getAddress());
    }

    @Test
    void shouldReturnUserDetailsByNameAndLastname() {
        var userCreateDto = TestDtoBuilder.createUserCreateDTO();
        var userDetailsDto = userService.create(userCreateDto).getUserDetailsDto();

        var userDetails = userDetailsService.getAllByNameAndLastname("Semen", "Kobelev");

        assertThat(userDetails).hasSize(1);
        assertThat(userDetails.get(0).getAddress()).isEqualTo(userDetailsDto.getAddress());
    }

    @Test
    void shouldReturnUserDetailsById() {
        var userCreateDto = TestDtoBuilder.createUserCreateDTO();
        var expectedUserDetails = userService.create(userCreateDto).getUserDetailsDto();

        var actualUserDetails = userDetailsService.getById(expectedUserDetails.getId());

        assertThat(actualUserDetails).isNotNull();
        assertEquals(expectedUserDetails, actualUserDetails);
    }

    @Test
    void shouldUpdateUserDetailsCorrectly() {
        var userCreateDto = TestDtoBuilder.createUserCreateDTO();
        var userDetailsUpdateDto = new UserDetailsUpdateDto(
                "test",
                "test",
                "Moscow",
                "1111111111");

        var savedUserDetails = userService.create(userCreateDto).getUserDetailsDto();

        var actualUserDetails = userDetailsService.update(savedUserDetails.getId(), userDetailsUpdateDto);

        assertThat(actualUserDetails).isNotNull();
        assertEquals(userDetailsUpdateDto.getName(), actualUserDetails.getName());
        assertEquals(userDetailsUpdateDto.getLastname(), actualUserDetails.getLastname());
        assertSame(userDetailsUpdateDto.getAddress(), actualUserDetails.getAddress());
        assertSame(userDetailsUpdateDto.getPhone(), actualUserDetails.getPhone());
    }

    @Test
    void shouldDeleteUserDetailByIdCorrectly() {
        assertTrue(userDetailsService.deleteById(UserDetailsBuilder.TEST_USER_DETAILS_ID_FOR_DELETE));
    }

    @Test
    void shouldNotDeleteUserWithNonExistsId() {
        assertFalse(userDetailsService.deleteById(999999L));
    }
}