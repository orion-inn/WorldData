package com.javatraining.worlddata.controller;

import com.javatraining.worlddata.entity.City;
import com.javatraining.worlddata.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cities")
public class CityRestController {

    @Autowired
    private CityService service;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    City create(@RequestBody City city) {
        return service.create(city);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    City update(@PathVariable int id, @RequestBody City city) {
        return service.update(id, city);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable int id) {
        service.delete(id);
    }

    @GetMapping("/{id}")
    City getById(@PathVariable int id) {
        return service.getById(id);
    }

    @GetMapping("/city")
    City getByName(@RequestParam String name) {
        return service.getByName(name);
    }

    @GetMapping("")
    List<City> getAll() {
        return service.getAll();
    }
}
