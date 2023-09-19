package com.dmdev.http.controller;

import com.dmdev.dto.CarFilterDto;
import com.dmdev.dto.CarCreateDto;
import com.dmdev.dto.CarUpdateDto;
import com.dmdev.service.CarService;
import com.dmdev.service.ModelService;
import com.dmdev.service.exception.BadRequestException;
import com.dmdev.service.exception.NotFoundException;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;


@Controller
@RequestMapping(path = "/cars")
@RequiredArgsConstructor
public class CarController {

    private static final String SUCCESS_ATTRIBUTE = "success_message";

    private final CarService carService;
    private final ModelService modelService;

    @GetMapping("/create")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String createCar(Model model, @ModelAttribute CarCreateDto car) {
        model.addAttribute("car", car);
        model.addAttribute("models", modelService.getAll());
        return "layout/car/car";
    }

    @PostMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String create(@ModelAttribute CarCreateDto requestDto,
                         RedirectAttributes redirectedAttributes) {
        return Optional.of(carService.create(requestDto))
                .map(car -> {
                    redirectedAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, "You create car successfully.");
                    return "redirect:/cars";
                }).orElseThrow(() -> new BadRequestException("Can not create car. Please check input parameters"));
    }

    @PostMapping("/{id}/update")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute CarUpdateDto requestDto) {
        return Optional.of(carService.update(id, requestDto))
                .map(car -> "redirect:/cars/{id}")
                .orElseThrow(() -> new BadRequestException("Can not update car. Please check input parameters"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('CLIENT', 'ADMIN')")
    public String findById(@PathVariable("id") Long id, Model model) {
        return Optional.of(carService.getById(id))
                .map(car -> {
                    model.addAttribute("car", car);
                    model.addAttribute("models", modelService.getAll());
                    return "layout/car/car";
                })
                .orElseThrow(() -> new NotFoundException(NotFoundException.prepare("Car", "id", id)));
    }

    @GetMapping("/by-number")
    @PreAuthorize("hasAnyAuthority('CLIENT', 'ADMIN')")
    public String findBySerialNumber(@RequestParam("serialNumber") String serialNumber, Model model) {
        var car = carService.getByCarSerialNumber(serialNumber);
        model.addAttribute("car", car);
        return "layout/car/car";
    }

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT')")
    public String findAll(Model model,
                          @ModelAttribute CarFilterDto carFilterDto,
                          @RequestParam(required = false, defaultValue = "1") Integer page,
                          @RequestParam(required = false, defaultValue = "20") Integer size) {
        var carPage = carService.getAll(carFilterDto, page - 1, size);
        model.addAttribute("carPage", carPage);
        model.addAttribute("filter", carFilterDto);
        model.addAttribute("modelNames", modelService.getAll());

        return "layout/car/cars";
    }

    @GetMapping("/with-accidents")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String findAllWithDamages(Model model,
                                       @ModelAttribute CarFilterDto carFilter) {
        var cars = carService.getAllWithDamages();
        var carPage = new PageImpl<>(cars);
        model.addAttribute("carPage", carPage);
        model.addAttribute("filter", carFilter);
        model.addAttribute("modelNames", modelService.getAll());

        return "layout/car/cars";
    }

    @GetMapping("/without-accidents")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String findAllWithoutDamages(Model model,
                                          @ModelAttribute CarFilterDto carFilter) {
        var cars = carService.getAllWithoutDamages();
        var carPage = new PageImpl<>(cars);
        model.addAttribute("carPage", carPage);
        model.addAttribute("filter", carFilter);
        model.addAttribute("modelNames", modelService.getAll());

        return "layout/car/cars";
    }

    @GetMapping("/under-repair")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String findAllIsNew(Model model,
                                     @ModelAttribute CarFilterDto carFilterDto) {
        var cars = carService.getAllIsNew();
        var carPage = new PageImpl<>(cars);
        model.addAttribute("carPage", carPage);
        model.addAttribute("filter", carFilterDto);
        model.addAttribute("modelNames", modelService.getAll());

        return "layout/car/cars";
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String delete(@PathVariable("id") Long id) {
        if (!carService.deleteById(id)) {
            throw new NotFoundException(NotFoundException.prepare("Car", "id", id));
        }
        return "redirect:/cars";
    }
}