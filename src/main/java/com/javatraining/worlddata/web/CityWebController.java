package com.javatraining.worlddata.web;

import com.javatraining.worlddata.entity.City;
import com.javatraining.worlddata.exception.ResourceAlreadyExistsException;
import com.javatraining.worlddata.exception.ResourceNotFoundException;
import com.javatraining.worlddata.service.CityService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/cities")
public class CityWebController {

    @Autowired
    CityService service;

    @GetMapping("")
    String showCities(Model model,
            @PathParam("id") Integer id,
            @PathParam("country") String country) {
        String title;
        List<City> cities;

        if (id != null) {
            City city = service.getById(id);
            title = city.getName();
            cities = List.of(city);
        } else if (country != null) {
            title = "Cities in " + country;
            cities = service.getAllByCountryName(country);
        } else {
            title = "Cities";
            cities = service.getAll();
        }

        model.addAttribute("title", title);
        model.addAttribute("cities", cities);

        return "cities";
    }

    @PostMapping("/save")
    String saveCity(City city, RedirectAttributes attributes) {
        try {
            City savedCity = service.create(city);
            attributes.addFlashAttribute(
                    "message",
                    "City with id=" + savedCity.getId() + " created");
        } catch (ResourceAlreadyExistsException e) {
            attributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/cities";
    }

    @PostMapping("/edit")
    String editCity(City city, RedirectAttributes attributes) {
        try {
            City updatedCity = service.update(city.getId(), city);
            attributes.addFlashAttribute(
                    "message",
                    "City with id=" + updatedCity.getId() + " edited");
        } catch (ResourceNotFoundException e) {
            attributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/cities";
    }

    @GetMapping("/delete/{id}")
    String deleteCity(@PathVariable int id, RedirectAttributes attributes) {
        try {
            service.delete(id);
            attributes.addFlashAttribute(
                    "message",
                    "City with id=" + id + " deleted");
        } catch (ResourceNotFoundException e) {
            attributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/cities";
    }
}
