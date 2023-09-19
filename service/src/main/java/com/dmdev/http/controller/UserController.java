package com.dmdev.http.controller;

import com.dmdev.dto.UserFilterDto;
import com.dmdev.dto.UserChangePasswordDto;
import com.dmdev.dto.UserCreateDto;
import com.dmdev.dto.UserUpdateDto;
import com.dmdev.entity.Role;
import com.dmdev.service.UserService;
import com.dmdev.service.exception.BadRequestException;
import com.dmdev.service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final static String SUCCESS_ATTRIBUTE = "success_message";
    private final static String FAILURE_ATTRIBUTE = "errors";
    private final UserService userService;

    @PostMapping("/sign-up")
    public String create(@ModelAttribute("registration") @Valid UserCreateDto userCreateDto,
                         BindingResult bindingResult,
                         RedirectAttributes redirectedAttributes) {
        if (bindingResult.hasErrors()) {
            String errorMessages = bindingResult.getAllErrors().stream()
                    .map(error -> ((FieldError) error).getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining("<br>"));
            redirectedAttributes.addFlashAttribute(FAILURE_ATTRIBUTE, errorMessages);
            return "redirect:/home";
        }
        return Optional.of(userService.create(userCreateDto))
                .map(user -> {
                    redirectedAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, "Your registration was successfully. Please login");
                    return "redirect:/home";
                }).orElseThrow(() -> new BadRequestException("Can not create user. Please check input parameters"));
    }

    @PostMapping("/{id}/update")
    @PreAuthorize("hasAnyAuthority('CLIENT', 'ADMIN')")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute("updateUser") @Valid UserUpdateDto userUpdateDto) {
        return Optional.of(userService.update(id, userUpdateDto)).map(result -> "redirect:/users/{id}")
                .orElseThrow(() -> new BadRequestException("Can not update user. Please check input parameters"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String findById(@PathVariable("id") Long id, Model model) {
        return Optional.of(userService.getById(id))
                .map(user -> {
                    model.addAttribute("user", user);
                    model.addAttribute("roles", Role.values());
                    return "layout/user/user";
                })
                .orElseThrow(() -> new NotFoundException(NotFoundException.prepare("User", "id", id)));
    }

    @GetMapping("/profile/{id}")
    @PreAuthorize("hasAnyAuthority('CLIENT', 'ADMIN')")
    public String findProfileById(@PathVariable("id") Long id, Model model) {
        return Optional.of(userService.getById(id))
                .map(user -> {
                    model.addAttribute("user", user);
                    model.addAttribute("roles", Role.values());
                    return "layout/user/profile";
                })
                .orElseThrow(() -> new NotFoundException(NotFoundException.prepare("User", "id", id)));
    }

    @PostMapping("/{id}/change-password")
    @PreAuthorize("hasAnyAuthority('CLIENT', 'ADMIN')")
    public String changePassword(@PathVariable("id") Long id,
                                 @ModelAttribute @Valid UserChangePasswordDto changedPasswordDto) {
        return Optional.of(userService.changePassword(id, changedPasswordDto))
                .map(result -> "redirect:/users/{id}")
                .orElseThrow(() -> new BadRequestException("Password have not been changed. Please check if old password is correct"));
    }

    @PostMapping("/{id}/change-role")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String changeRole(@PathVariable("id") Long id,
                             @PathParam(value = "role") Role role) {
        return Optional.of(userService.changeRole(id, role))
                .map(result -> "redirect:/users")
                .orElseThrow(() -> new BadRequestException("Role have not been changed"));
    }

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('CLIENT', 'ADMIN')")
    public String findAll(Model model,
                          @ModelAttribute @Nullable @Valid UserFilterDto userFilterDto,
                          @RequestParam(required = false, defaultValue = "1") Integer page,
                          @RequestParam(required = false, defaultValue = "20") Integer size) {
        var usersPage = userService.getAll(userFilterDto, page - 1, size);
        model.addAttribute("usersPage", usersPage);
        model.addAttribute("filter", userFilterDto);
        model.addAttribute("roles", Role.values());
        return "layout/user/users";
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String delete(@PathVariable("id") Long id) {
        if (!userService.deleteById(id)) {
            throw new NotFoundException(NotFoundException.prepare("User", "id", id));

        }
        return "redirect:/users";
    }
}
