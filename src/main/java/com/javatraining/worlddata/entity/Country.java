package com.javatraining.worlddata.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "Country")
public class Country implements Serializable {

	@Id
	@Column(name = "code", unique = true, nullable = false)
	private String code;

	@Column(name = "name", unique = true, nullable = false)
	private String name;

	@Column(name = "continent")
	private String continent;

	@Column(name = "population")
	private int population;

	@Column(name = "surface_area")
	private double surfaceArea;

	@Column(name = "gnp")
	private double gnp;

	@Column(name = "capital_city")
	private int capitalCity;

	public Country() {
	}

	public Country(String code, String name, String continent, int population, double surfaceArea, double gnp, int capitalCity) {
		this.code = code;
		this.name = name;
		this.continent = continent;
		this.population = population;
		this.surfaceArea = surfaceArea;
		this.gnp = gnp;
		this.capitalCity = capitalCity;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContinent() {
		return continent;
	}

	public void setContinent(String continent) {
		this.continent = continent;
	}

	public int getPopulation() {
		return population;
	}

	public void setPopulation(int population) {
		this.population = population;
	}

	public double getSurfaceArea() {
		return surfaceArea;
	}

	public void setSurfaceArea(double surfaceArea) {
		this.surfaceArea = surfaceArea;
	}

	public double getGnp() {
		return gnp;
	}

	public void setGnp(double gnp) {
		this.gnp = gnp;
	}

	public int getCapitalCity() {
		return capitalCity;
	}

	public void setCapitalCity(int capitalCity) {
		this.capitalCity = capitalCity;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Country country = (Country) o;

		return code.equals(country.code);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(code);
	}

	@Override
	public String toString() {
		return "Country:  " +
				"name='" + name + "'  " +
				"continent='" + continent + "'  " +
				"population=" + population + "  " +
				"surfaceArea=" + surfaceArea;
	}
}