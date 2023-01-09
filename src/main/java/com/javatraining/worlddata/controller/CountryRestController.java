package com.javatraining.worlddata.controller;

import com.javatraining.worlddata.entity.Country;
import com.javatraining.worlddata.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/countries")
public class CountryRestController {

    @Autowired
    private CountryService service;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    Country create(@RequestBody Country country) {
        return service.create(country);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    Country update(@PathVariable int id, @RequestBody Country country) {
        return service.update(id, country);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable int id) {
        service.delete(id);
    }

    @GetMapping("/{id}")
    Country getById(@PathVariable int id) {
        return service.getById(id);
    }

    @GetMapping("/country")
    Country getByName(@RequestParam String name) {
        return service.getByName(name);
    }

    @GetMapping("")
    List<Country> getAll() {
        return service.getAll();
    }
}
