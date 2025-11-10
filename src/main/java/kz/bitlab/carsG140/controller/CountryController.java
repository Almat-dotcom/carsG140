package kz.bitlab.carsG140.controller;

import kz.bitlab.carsG140.entity.Country;
import kz.bitlab.carsG140.repository.CountryRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/country")
public class CountryController {
    private final CountryRepository countryRepository;

    @GetMapping
    public String countries() {
        return "add-country";
    }

    @PostMapping("/addcountry")
    public String addCountry(@RequestParam(name = "country_name") String name,
                             @RequestParam(name = "country_code") String code) {
        Country country = Country.builder()
                .name(name)
                .code(code)
                .build();
        countryRepository.save(country);
        return "redirect:/cars";
    }
}
