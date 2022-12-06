package com.javatraining.worlddata.helper;

import java.util.Optional;
import com.javatraining.worlddata.entity.City;

/**
 * @author Binnur Kurt <binnur.kurt@gmail.com>
 */
public class ContinentCityPair implements Comparable<ContinentCityPair>{

	private String continent;
	private City city;

	public ContinentCityPair(String continent, City city) {
		this.continent = continent;
		this.city = city;
	}

	public String getContinent() {
		return continent;
	}

	public City getCity() {
		return city;
	}

	@Override
	public int compareTo(ContinentCityPair other) {
		return this.city.getPopulation()-other.city.getPopulation();
	}

	public static void printEntry(String continent, Optional<ContinentCityPair> pair) {
		System.out.printf("%s: %s\n",continent,pair.get().getCity());
	}

}
