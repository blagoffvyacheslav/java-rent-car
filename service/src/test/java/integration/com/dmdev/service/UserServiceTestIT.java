package integration.com.dmdev.service;

import com.dmdev.dto.UserFilterDto;
import com.dmdev.dto.UserUpdateDto;
import com.dmdev.dto.UserReadDto;
import com.dmdev.entity.Role;
import com.dmdev.service.UserService;
import com.dmdev.service.exception.BadRequestException;
import integration.com.dmdev.IntegrationBaseTest;
import utils.builder.TestDtoBuilder;
import utils.builder.UserBuilder;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
class UserServiceTestIT extends IntegrationBaseTest {

    private  final UserService userService;

    @Test
    void shouldSaveUserCorrectly() {
        var userCreateRequestDTO = TestDtoBuilder.createUserCreateDTO();

        var actualUser = userService.create(userCreateRequestDTO);

        assertEquals(userCreateRequestDTO.getName(), actualUser.getUserDetailsDto().getName());
        assertEquals(userCreateRequestDTO.getLastname(), actualUser.getUserDetailsDto().getLastname());
        assertEquals(userCreateRequestDTO.getEmail(), actualUser.getEmail());
        assertEquals(userCreateRequestDTO.getUsername(), actualUser.getUsername());
        assertEquals(userCreateRequestDTO.getDriverLicenseNumber(), actualUser.getDriverLicenseDto().getLicenseNumber());
        assertSame(Role.ADMIN, actualUser.getRole());
    }

    @Test
    void shouldThrowExceptionWhenSaveUserWithExistsEmail() {
        var userCreateRequestDTO = TestDtoBuilder.createUserCreateDTOWithExistsEmail();

        var result = assertThrowsExactly(BadRequestException.class, () -> userService.create(userCreateRequestDTO));

        assertEquals(400, result.getRawStatusCode());
    }


    @Test
    void shouldFindAllUsers() {
        Page<UserReadDto> users = userService.getAll(UserFilterDto.builder().build(), 0, 4);

        assertThat(users.getContent()).hasSize(2);
        assertThat(users.getTotalElements()).isEqualTo(2L);
        assertThat(users.getNumberOfElements()).isEqualTo(2L);

        List<String> emails = users.getContent().stream().map(UserReadDto::getEmail).collect(toList());
        assertThat(emails).containsExactlyInAnyOrder("client@client.com", "admin@gmail.com");
    }

    @Test
    void shouldReturnUserById() {
        var userCreateRequestDto = TestDtoBuilder.createUserCreateDTO();
        var expectedUser = userService.create(userCreateRequestDto);

        var actualUser = userService.getById(expectedUser.getId());

        assertThat(actualUser).isNotNull();
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void shouldUpdateUserCorrectly() {
        var userCreateRequestDto = TestDtoBuilder.createUserCreateDTO();
        var userUpdateRequestDto = new UserUpdateDto(
                "test1@gmal.com",
                "test",
                Role.CLIENT);
        var savedUser = userService.create(userCreateRequestDto);

        var actualUser = userService.update(savedUser.getId(), userUpdateRequestDto);

        assertThat(actualUser).isNotNull();
        assertEquals(userUpdateRequestDto.getEmail(), actualUser.getEmail());
        assertEquals(userUpdateRequestDto.getUsername(), actualUser.getUsername());
        assertSame(userUpdateRequestDto.getRole(), actualUser.getRole());
    }

    @Test
    void shouldDeleteUserByIdCorrectly() {
        assertTrue(userService.deleteById(UserBuilder.TEST_EXISTS_USER_ID));
    }

    @Test
    void shouldNotDeleteUserWithNonExistsId() {
        assertFalse(userService.deleteById(999999L));
    }
}