package com.dmdev.http.controller;

import com.dmdev.dto.DriverLicenseCreateDto;
import com.dmdev.dto.DriverLicenseUpdateDto;
import com.dmdev.service.DriverLicenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
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

@Controller
@RequestMapping(path = "/driver-licenses")
@RequiredArgsConstructor
public class DriverLicenseController {

    private static final String SUCCESS_ATTRIBUTE = "success_message";

    private final DriverLicenseService driverLicenseService;

    @PostMapping()
    public String create(@ModelAttribute DriverLicenseCreateDto requestDto,
                         RedirectAttributes redirectedAttributes) {
        return driverLicenseService.create(requestDto)
                .map(driverLicense -> {
                    redirectedAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, "You create driver license successfully.");
                    return "redirect:/users/" + driverLicense.getUserId();
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute DriverLicenseUpdateDto requestDto) {
        return driverLicenseService.update(id, requestDto)
                .map(driverLicense -> "redirect:/users/" + driverLicense.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model) {
        return driverLicenseService.getById(id)
                .map(driverLicense -> {
                    model.addAttribute("driverLicense", driverLicense);
                    return "layout/user/driver-license";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/by-user-id")
    public String findByUserId(@RequestParam() Long id, Model model) {
        return driverLicenseService.getByUserId(id)
                .map(driverLicense -> {
                    model.addAttribute("driverLicense", driverLicense);
                    return "layout/user/driver-license";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping()
    public String findAll(Model model,
                          @RequestParam(required = false, defaultValue = "1") Integer page,
                          @RequestParam(required = false, defaultValue = "20") Integer size) {

        var driverLicensesPage = driverLicenseService.getAll(page - 1, size);
        model.addAttribute("driverLicensesPage", driverLicensesPage);

        return "layout/user/driver-licenses";
    }

    @GetMapping("/by-number")
    public String findByNumber(Model model, @RequestParam(required = false) String number) {
        var driverLicenses = driverLicenseService.getByNumber(number);
        var driverLicensesPage = new PageImpl<>(driverLicenses);
        model.addAttribute("driverLicensesPage", driverLicensesPage);

        return "layout/user/driver-licenses";
    }

    @GetMapping("/expired")
    public String findAllExpiredLicenses(Model model) {
        var driverLicenses = driverLicenseService.getAllExpiredDriverLicenses();
        var driverLicensesPage = new PageImpl<>(driverLicenses);
        model.addAttribute("driverLicensesPage", driverLicensesPage);

        return "layout/user/driver-licenses";
    }


    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        if (!driverLicenseService.deleteById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/driver-licenses";
    }
}