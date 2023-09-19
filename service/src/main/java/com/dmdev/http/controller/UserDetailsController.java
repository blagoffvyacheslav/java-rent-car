package com.dmdev.http.controller;

import com.dmdev.dto.UserDetailsCreateDto;
import com.dmdev.dto.UserDetailsFilterDto;
import com.dmdev.dto.UserDetailsUpdateDto;
import com.dmdev.service.UserDetailsService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
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

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping(path = "/user-details")
@RequiredArgsConstructor
public class UserDetailsController {

    private static final String SUCCESS_ATTRIBUTE = "success_message";
    private final UserDetailsService userDetailsService;

    @PostMapping()
    public String create(@ModelAttribute @Valid UserDetailsCreateDto requestDto,
                         RedirectAttributes redirectedAttributes) {
        return Optional.of(userDetailsService.create(requestDto))
                .map(userDetails -> {
                    redirectedAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, "You create details successfully.");
                    return "redirect:/users/" + userDetails.getUserId();
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute @Valid UserDetailsUpdateDto requestDto) {
        return Optional.of(userDetailsService.update(id, requestDto))
                .map(userDetails -> "redirect:/users/" + userDetails.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model) {
        return Optional.of(userDetailsService.getById(id))
                .map(userDetails -> {
                    model.addAttribute("userDetails", userDetails);
                    return "layout/user/user";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/by-user-id")
    public String findByUserId(@RequestParam() Long id, Model model) {
        return userDetailsService.getByUserId(id)
                .map(userDetails -> {
                    model.addAttribute("userDetails", userDetails);
                    return "layout/user/user";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping()
    public String findAll(Model model,
                          @ModelAttribute @Nullable @Valid UserDetailsFilterDto userDetailsFilterDto,
                          @RequestParam(required = false, defaultValue = "1") Integer page,
                          @RequestParam(required = false, defaultValue = "20") Integer size) {
        var usersDetailsPage = userDetailsService.getAll(userDetailsFilterDto, page - 1, size);
        model.addAttribute("filter", userDetailsFilterDto);
        model.addAttribute("usersDetailsPage", usersDetailsPage);
        return "layout/user/user";
    }

    @GetMapping("/by-name-surname")
    public String findAllByUserNameAndSurname(Model model,
                                              @ModelAttribute @Nullable @Valid UserDetailsFilterDto userDetailsFilterDto,
                                              @RequestParam(required = false) String name,
                                              @RequestParam(required = false) String surname) {
        var usersDetails = userDetailsService.getAllByNameAndLastname(name, surname);
        var usersDetailsPage = new PageImpl<>(usersDetails);
        model.addAttribute("filter", userDetailsFilterDto);
        model.addAttribute("usersDetailsPage", usersDetailsPage);
        return "layout/user/user";
    }

    @GetMapping("/by-registration-date")
    public String findAllByRegistrationDate(Model model,
                                            @ModelAttribute @Nullable @Valid UserDetailsFilterDto userDetailsFilterDto,
                                            @RequestParam(required = false) LocalDate registrationDate) {
        var usersDetails = userDetailsService.getAllByRegistrationDate(registrationDate);
        var usersDetailsPage = new PageImpl<>(usersDetails);
        model.addAttribute("filter", userDetailsFilterDto);
        model.addAttribute("usersDetailsPage", usersDetailsPage);
        return "layout/user/user";
    }

    @GetMapping("/by-registration-dates")
    public String findAllByRegistrationDates(Model model,
                                             @ModelAttribute @Nullable UserDetailsFilterDto userDetailsFilterDto,
                                             @RequestParam(required = false) LocalDate from,
                                             @RequestParam(required = false) LocalDate to) {
        var usersDetails = userDetailsService.getAllByRegistrationDates(from, to);
        var usersDetailsPage = new PageImpl<>(usersDetails);
        model.addAttribute("filter", userDetailsFilterDto);
        model.addAttribute("usersDetailsPage", usersDetailsPage);
        return "layout/user/user";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        if (!userDetailsService.deleteById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/user-details";
    }

}