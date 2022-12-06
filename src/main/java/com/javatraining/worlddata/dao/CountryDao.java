package com.javatraining.worlddata.dao;

import com.javatraining.worlddata.entity.Country;
import java.util.List;

/**
 *
 * @author Binnur Kurt (binnur.kurt@gmail.com)
 */
public interface CountryDao {
	Country addCountry(Country country);
	Country removeCountry(Country country);
	Country updateCountry(Country country);
	Country findCountryByCode(String code);
	List<Country> findAllCountries();
}
