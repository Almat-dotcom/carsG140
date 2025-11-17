package kz.bitlab.carsG140.service.impl;

import kz.bitlab.carsG140.entity.Country;
import kz.bitlab.carsG140.repository.CountryRepository;
import kz.bitlab.carsG140.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;

    @Override
    public List<Country> findAll() {
        return countryRepository.findAll();
    }

    @Override
    public void addCountry(String name, String code) {
        Country country = Country.builder()
                .name(name)
                .code(code)
                .build();
        countryRepository.save(country);
    }
}
