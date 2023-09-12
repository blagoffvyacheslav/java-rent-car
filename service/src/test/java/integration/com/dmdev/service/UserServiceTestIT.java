package integration.com.dmdev.service;

import com.dmdev.dto.UserFilterDto;
import com.dmdev.dto.UserUpdateDto;
import com.dmdev.dto.UserReadDto;
import com.dmdev.entity.Role;
import com.dmdev.service.UserService;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.dto.TestDto;
import integration.com.dmdev.entity.UserTestIT;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.web.server.ResponseStatusException;

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
        var userCreateRequestDTO = TestDto.createUserCreateDTO();

        var actualUser = userService.create(userCreateRequestDTO);

        assertTrue(actualUser.isPresent());
        assertEquals(userCreateRequestDTO.getName(), actualUser.get().getUserDetailsDto().getName());
        assertEquals(userCreateRequestDTO.getLastname(), actualUser.get().getUserDetailsDto().getLastname());
        assertEquals(userCreateRequestDTO.getEmail(), actualUser.get().getEmail());
        assertEquals(userCreateRequestDTO.getUsername(), actualUser.get().getUsername());
        assertEquals(userCreateRequestDTO.getDriverLicenseNumber(), actualUser.get().getDriverLicenseDto().getLicenseNumber());
        assertSame(Role.ADMIN, actualUser.get().getRole());
    }

    @Test
    void shouldThrowExceptionWhenSaveUserWithExistsEmail() {
        var userCreateRequestDTO = TestDto.createUserCreateDTOWithExistsEmail();

        var result = assertThrowsExactly(ResponseStatusException.class, () -> userService.create(userCreateRequestDTO));

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
        var userCreateRequestDto = TestDto.createUserCreateDTO();
        var expectedUser = userService.create(userCreateRequestDto);

        var actualUser = userService.getById(expectedUser.get().getId());

        assertThat(actualUser).isNotNull();
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void shouldUpdateUserCorrectly() {
        var userCreateRequestDto = TestDto.createUserCreateDTO();
        var userUpdateRequestDto = new UserUpdateDto(
                "test1@gmal.com",
                "test",
                Role.CLIENT);
        var savedUser = userService.create(userCreateRequestDto);

        var actualUser = userService.update(savedUser.get().getId(), userUpdateRequestDto);

        assertThat(actualUser).isNotNull();
        actualUser.ifPresent(user -> {
            assertEquals(userUpdateRequestDto.getEmail(), user.getEmail());
            assertEquals(userUpdateRequestDto.getUsername(), user.getUsername());
            assertSame(userUpdateRequestDto.getRole(), user.getRole());
        });
    }

    @Test
    void shouldDeleteUserByIdCorrectly() {
        assertTrue(userService.deleteById(UserTestIT.TEST_EXISTS_USER_ID));
    }

    @Test
    void shouldNotDeleteUserWithNonExistsId() {
        assertFalse(userService.deleteById(999999L));
    }
}