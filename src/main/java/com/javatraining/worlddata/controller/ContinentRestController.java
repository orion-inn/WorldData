package com.javatraining.worlddata.controller;

import com.javatraining.worlddata.entity.Continent;
import com.javatraining.worlddata.service.ContinentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/continents")
public class ContinentRestController {

    @Autowired
    private ContinentService service;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    Continent create(@RequestBody Continent continent) {
        return service.create(continent);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    Continent update(@PathVariable int id, @RequestBody Continent continent) {
        return service.update(id, continent);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable int id) {
        service.delete(id);
    }

    @GetMapping("/{id}")
    Continent getById(@PathVariable int id) {
        return service.getById(id);
    }

    @GetMapping("/continent")
    Continent getByName(@RequestParam String name) {
        return service.getByName(name);
    }

    @GetMapping("")
    List<Continent> getAll() {
        return service.getAll();
    }
}
