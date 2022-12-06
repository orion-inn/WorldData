package com.javatraining.worlddata.dao;

import com.javatraining.worlddata.entity.City;
import com.javatraining.worlddata.entity.Country;
import com.javatraining.worlddata.helper.CountryCityCountPair;

import java.util.*;
import java.util.concurrent.*;

/**
 *
 * @author Binnur Kurt (binnur.kurt@gmail.com)
 */
public class InMemoryWorldDao implements WorldDao {
	private Set<String> continents;
	private Map<String, Country> countries;
	private Map<Integer, City> cities;

	private static InMemoryWorldDao instance;

	public static InMemoryWorldDao getInstance() {
		synchronized (InMemoryWorldDao.class) {
			if (instance == null)
				instance = new InMemoryWorldDao();
		}
		return instance;
	}

	private InMemoryWorldDao() {
		continents = new HashSet<>();
		countries = new ConcurrentHashMap<>();
		cities = new ConcurrentHashMap<>();
	}

	public Set<String> getContinents() {
		return continents;
	}

	public void setContinents(Set<String> continents) {
		this.continents = new HashSet<>(continents);
	}

	public Map<String, Country> getCountries() {
		return countries;
	}

	public void setCountries(Map<String, Country> countries) {
		this.countries = new ConcurrentHashMap<>(countries);
	}

	public Map<Integer, City> getCities() {
		return cities;
	}

	public void setCities(Map<Integer, City> cities) {
		this.cities = new ConcurrentHashMap<>(cities);
	}

	@Override
	public Country addCountry(Country country) {
		if (!countries.containsKey(country.getCode())) {
			continents.add(country.getContinent());
			countries.put(country.getCode(), country);
			return country;
		}
		return null;
	}

	@Override
	public Country removeCountry(Country country) {
		return countries.remove(country.getCode());
	}

	@Override
	public Country updateCountry(Country country) {
		if (countries.containsKey(country.getCode())) {
			return countries.put(country.getCode(), country);
		}
		return null;
	}

	@Override
	public Country findCountryByCode(String code) {
		return countries.get(code);
	}

	@Override
	public List<Country> findAllCountries() {
		return new ArrayList<>(countries.values());
	}

	@Override
	public City addCity(City city) {
		if (!cities.containsKey(city.getId())) {
			cities.put(city.getId(), city);
			return city;
		}
		return null;
	}

	@Override
	public City removeCity(City city) {
		return cities.remove(city.getId());
	}

	@Override
	public City updateCity(City city) {
		if (cities.containsKey(city.getId())) {
			return cities.put(city.getId(), city);
		}
		return null;
	}

	@Override
	public City findCityById(int id) {
		return cities.get(id);
	}

	// Exercise4
	public City findHighestPopulatedCapitalCity() {
		return findAllCountries().stream()
				.map(country -> findCityById(country.getCapital()))
				.filter(Objects::nonNull)
				.max(Comparator.comparing(City::getPopulation))
				.orElse(null);
	}

	// Exercise6
	public List<CountryCityCountPair> getCountriesSortedByCityCountInDescOrder() {
		return findAllCountries().stream()
				.map(country -> new CountryCityCountPair(country, country.getCities().size()))
				.sorted(Comparator.comparing(CountryCityCountPair::getCount).reversed())
				.toList();
	}
}
