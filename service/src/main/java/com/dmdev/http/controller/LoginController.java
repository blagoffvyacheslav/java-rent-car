package com.dmdev.api.controller;

import com.dmdev.dto.LoginDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String getLogin() {
        return "layout/user/login";
    }

//    @PostMapping("/login")
//    public String login(@ModelAttribute @Valid LoginDto loginDto,
//                        RedirectAttributes redirectedAttributes,
//                        HttpServletRequest request) {
//        return userService.login(loginDto)
//                .map(user -> {
//                            request.getSession().setAttribute("user", user);
//                            redirectedAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, "Your login was successfully. Now you can choose car");
//                            return "redirect:/home";
//                        }
//                ).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, ("User with these credentials does not exist. Please try again")));
//    }
}