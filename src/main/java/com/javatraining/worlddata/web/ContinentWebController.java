package com.javatraining.worlddata.web;

import com.javatraining.worlddata.entity.Continent;
import com.javatraining.worlddata.exception.ResourceAlreadyExistsException;
import com.javatraining.worlddata.exception.ResourceNotFoundException;
import com.javatraining.worlddata.service.ContinentService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/continents")
public class ContinentWebController {

    @Autowired
    ContinentService service;

    @GetMapping("")
    String showContinents(Model model,
                          @PathParam("id") Integer id) {
        String title;
        List<Continent> continents;

        if (id != null) {
            Continent continent = service.getById(id);
            title = continent.getName();
            continents = List.of(continent);
        } else {
            title = "Continents";
            continents = service.getAll();
        }

        model.addAttribute("title", title);
        model.addAttribute("continents", continents);

        return "continents";
    }

    @PostMapping("/save")
    String saveContinent(Continent continent, RedirectAttributes attributes) {
        try {
            Continent savedContinent = service.create(continent);
            attributes.addFlashAttribute(
                    "message",
                    "Continent with id=" + savedContinent.getId() + " created");
        } catch (ResourceAlreadyExistsException e) {
            attributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/continents";
    }

    @PostMapping("/edit")
    String editContinent(Continent continent, RedirectAttributes attributes) {
        try {
            Continent updatedContinent = service.update(continent.getId(), continent);
            attributes.addFlashAttribute(
                    "message",
                    "Continent with id=" + updatedContinent.getId() + " edited");
        } catch (ResourceNotFoundException e) {
            attributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/continents";
    }

    @GetMapping("/delete/{id}")
    String deleteContinent(@PathVariable int id, RedirectAttributes attributes) {
        try {
            service.delete(id);
            attributes.addFlashAttribute(
                    "message",
                    "Continent with id=" + id + " deleted");
        } catch (ResourceNotFoundException e) {
            attributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/continents";
    }
}
