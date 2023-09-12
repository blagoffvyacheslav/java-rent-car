package com.dmdev.http.rest;

import com.dmdev.dto.*;
import com.dmdev.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.notFound;

@Tag(name = "User API", description = "User API")
@RestController
@RequestMapping(path = "/api/v1/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create user")
    public UserReadDto create(@Parameter(required = true) @RequestBody @Valid UserCreateDto userCreateDto) {
        return userService.create(userCreateDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update user")
    public UserReadDto update(@Parameter(required = true) @PathVariable @Valid @NotNull Long id,
                              @Parameter(required = true) @RequestBody @Valid UserUpdateDto userUpdateDto) {
        return userService.update(id, userUpdateDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a user by id")
    public UserReadDto findById(@Parameter(required = true) @PathVariable @Valid @NotNull Long id) {
        return userService.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/change-password")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update user password")
    public UserReadDto changePassword(@Parameter(required = true) @PathVariable @Valid @NotNull Long id,
                                          @Parameter(required = true) @RequestBody @Valid UserChangePasswordDto changedPasswordDto) {
        return userService.changePassword(id, changedPasswordDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by id")
    public ResponseEntity<?> delete(@Parameter(required = true) @PathVariable @Valid Long id) {
        return userService.deleteById(id)
                ? noContent().build()
                : notFound().build();
    }
}