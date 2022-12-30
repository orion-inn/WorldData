package com.javatraining.worlddata.dao;

import com.javatraining.worlddata.entity.Country;
import java.util.List;

public interface CountryDao {
	void addCountry(Country country);
	void removeCountry(Country country);
	void updateCountry(Country country);
	Country findCountryByCode(String code);
	List<Country> findCountriesByPartialName(String name);
	List<Country> findAllCountries();
}
