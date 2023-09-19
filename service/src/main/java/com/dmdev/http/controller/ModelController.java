package com.dmdev.http.controller;

import com.dmdev.dto.ModelCreateDto;
import com.dmdev.dto.ModelFilterDto;
import com.dmdev.dto.ModelUpdateDto;
import com.dmdev.service.ModelService;
import com.dmdev.service.exception.BadRequestException;
import com.dmdev.service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
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

import javax.swing.text.html.Option;
import java.util.Optional;

@Controller
@RequestMapping(path = "/models")
@RequiredArgsConstructor
public class ModelController {

    private static final String SUCCESS_ATTRIBUTE = "success_message";

    private final ModelService modelService;

    @GetMapping("/create")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String createModel(Model model, @ModelAttribute ModelCreateDto carModel) {
        model.addAttribute("model", carModel);
        return "layout/model/create-model";
    }

    @PostMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String create(@ModelAttribute ModelCreateDto requestDto,
                         RedirectAttributes redirectedAttributes) {
        return Optional.of(modelService.create(requestDto))
                .map(car -> {
                    redirectedAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, "You create model successfully.");
                    return "redirect:/models";
                }).orElseThrow(() -> new BadRequestException("Can not create model. Please check input parameters"));
    }

    @PostMapping("/{id}/update")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute ModelUpdateDto requestDto) {
        return Optional.of(modelService.update(id, requestDto))
                .map(driverLicense -> "redirect:/models/{id}")
                .orElseThrow(() -> new BadRequestException("Can not update model. Please check input parameters"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('CLIENT', 'ADMIN')")
    public String findById(@PathVariable("id") Long id, Model model) {
        return Optional.of(modelService.getById(id))
                .map(carModel -> {
                    model.addAttribute("model", carModel);
                    return "layout/model/model";
                })
                .orElseThrow(() -> new NotFoundException(NotFoundException.prepare("Model", "id", id)));
    }

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT')")
    public String findAll(Model model,
                          @ModelAttribute ModelFilterDto modelFilterDto,
                          @RequestParam(required = false, defaultValue = "1") Integer page,
                          @RequestParam(required = false, defaultValue = "20") Integer size) {
        var modelPage = modelService.getAll(modelFilterDto, page - 1, size);
        model.addAttribute("modelPage", modelPage);
        model.addAttribute("models", modelService.getAll());
        model.addAttribute("filter", modelFilterDto);

        return "layout/model/models";
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String delete(@PathVariable("id") Long id) {
        if (!modelService.deleteById(id)) {
            throw new NotFoundException(NotFoundException.prepare("Model", "id", id));
        }
        return "redirect:/models";
    }
}