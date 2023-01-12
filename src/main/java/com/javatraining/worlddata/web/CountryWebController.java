package com.javatraining.worlddata.web;

import com.javatraining.worlddata.entity.City;
import com.javatraining.worlddata.entity.Country;
import com.javatraining.worlddata.exception.ResourceAlreadyExistsException;
import com.javatraining.worlddata.exception.ResourceNotFoundException;
import com.javatraining.worlddata.service.CountryService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/countries")
public class CountryWebController {

    @Autowired
    CountryService service;

    @GetMapping("")
    String showCountries(Model model,
                      @PathParam("id") Integer id,
                      @PathParam("continent") String continent) {
        String title;
        List<Country> countries;

        if (id != null) {
            Country country = service.getById(id);
            title = country.getName();
            countries = List.of(country);
        } else if (continent != null) {
            title = "Countries in " + continent;
            countries = service.getAllByContinentName(continent);
        } else {
            title = "Countries";
            countries = service.getAll();
        }

        model.addAttribute("title", title);
        model.addAttribute("countries", countries);

        return "countries";
    }

    @PostMapping("/save")
    String saveCountry(Country country, RedirectAttributes attributes) {
        try {
            Country savedCountry = service.create(country);
            attributes.addFlashAttribute(
                    "message",
                    "Country with id=" + savedCountry.getId() + " created");
        } catch (ResourceAlreadyExistsException e) {
            attributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/countries";
    }

    @PostMapping("/edit")
    String editCountry(Country country, RedirectAttributes attributes) {
        try {
            Country updatedCountry = service.update(country.getId(), country);
            attributes.addFlashAttribute(
                    "message",
                    "Country with id=" + updatedCountry.getId() + " edited");
        } catch (ResourceNotFoundException e) {
            attributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/countries";
    }

    @GetMapping("/delete/{id}")
    String deleteCountry(@PathVariable int id, RedirectAttributes attributes) {
        try {
            service.delete(id);
            attributes.addFlashAttribute(
                    "message",
                    "Country with id=" + id + " deleted");
        } catch (ResourceNotFoundException e) {
            attributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/countries";
    }
}
