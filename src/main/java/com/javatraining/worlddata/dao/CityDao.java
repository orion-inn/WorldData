package com.javatraining.worlddata.dao;

import com.javatraining.worlddata.entity.City;

import java.util.List;

public interface CityDao {
	void addCity(City city);
	void removeCity(City city);
	void updateCity(City city);
	City findCityById(int id);
	List<City> findAllCities();
}
