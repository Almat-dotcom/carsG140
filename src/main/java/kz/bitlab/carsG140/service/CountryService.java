package kz.bitlab.carsG140.service;

import kz.bitlab.carsG140.entity.Country;

import java.util.List;

public interface CountryService {
    List<Country> findAll();

    void addCountry(String name, String code);
}
