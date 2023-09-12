package com.dmdev.controllers;


import com.dmdev.dto.LoginDto;
import com.dmdev.dto.UserChangePasswordDto;
import com.dmdev.dto.UserCreateDto;
import com.dmdev.dto.UserUpdateDto;
import com.dmdev.dto.UserReadDto;
import com.dmdev.entity.Role;
import com.dmdev.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final static String SUCCESS_ATTRIBUTE = "success_message";
    private final UserService userService;
    @PostMapping()
    public String create(@ModelAttribute("registration") UserCreateDto userCreateDto,
                         RedirectAttributes redirectedAttributes) {
        return userService.create(userCreateDto)
                .map(user -> {
                    redirectedAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, "Your registration was successfully. Please login");
                    return "redirect:/home";
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ("Can not create user. Please check input parameters")));
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginDto loginDto,
                        RedirectAttributes redirectedAttributes,
                        HttpServletRequest request) {
        return userService.login(loginDto)
                .map(user -> {
                            request.getSession().setAttribute("user", user);
                            redirectedAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, "Your login was successfully. Now you can choose car");
                            return "redirect:/home";
                        }
                ).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, ("User with these credentials does not exist. Please try again")));
    }

    @PostMapping("/logout")
    public String logout(@ModelAttribute LoginDto loginDto,
                         RedirectAttributes redirectedAttributes,
                         HttpServletRequest request) {
        request.getSession().invalidate();
        redirectedAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, "You logout successfully");
        return "redirect:/home";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute("updateUser") UserUpdateDto userUpdateDto) {
        return userService.update(id, userUpdateDto)
                .map(result -> "redirect:/users/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ("Can not update user. Please check input parameters")));
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model) {
        return userService.getById(id)
                .map(user -> {
                    model.addAttribute("user", user);
                    model.addAttribute("roles", Role.values());
                    return "layout/user/profile";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, (String.format("User with id %s does not exist.", id))));
    }

    @PostMapping("/{id}/change-password")
    public String changePassword(@PathVariable("id") Long id,
                                 @ModelAttribute UserChangePasswordDto changedPasswordDto) {
        return userService.changePassword(id, changedPasswordDto)
                .map(result -> "redirect:/users/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, (String.format("Password have not been changed. Please check if old password is correct"))));
    }

    @GetMapping()
    public String findAll(Model model,
                          @RequestParam(required = false, defaultValue = "1") Integer page,
                          @RequestParam(required = false, defaultValue = "20") Integer size) {
        Page<UserReadDto> usersPage = userService.getAll(page - 1, size);
        model.addAttribute("usersPage", usersPage);

        return "layout/user/users";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        if (!userService.deleteById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, (String.format("User with id %s does not exist.", id)));

        }
        return "redirect:/users";
    }
}
