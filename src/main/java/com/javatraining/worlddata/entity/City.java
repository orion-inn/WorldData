package com.javatraining.worlddata.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "City")
public class City implements Serializable {

	@Id
	@Column(name = "id", unique = true, nullable = false)
	private int id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "population")
	private int population;

	@Column(name = "country_code")
	private String countryCode;

	public City() {
	}

	public City(int id, String name, String countryCode, int population) {
		this.id = id;
		this.name = name;
		this.population = population;
		this.countryCode = countryCode;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPopulation() {
		return population;
	}

	public void setPopulation(int population) {
		this.population = population;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		City city = (City) o;

		return id == city.id;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "City:  " +
				"name='" + name + "'  " +
				"countryCode='" + countryCode + "'  " +
				"population=" + population;
	}
}
