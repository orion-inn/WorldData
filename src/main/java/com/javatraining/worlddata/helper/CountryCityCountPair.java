package com.javatraining.worlddata.helper;

import com.javatraining.worlddata.entity.Country;

/**
 * @author Binnur Kurt <binnur.kurt@gmail.com>
 */
public class CountryCityCountPair {
  private Country country;
  private int count;

  public CountryCityCountPair(Country country, int count) {
    this.country = country;
    this.count = count;
  }

  public Country getCountry() {
    return country;
  }

  public int getCount() {
    return count;
  }
}
