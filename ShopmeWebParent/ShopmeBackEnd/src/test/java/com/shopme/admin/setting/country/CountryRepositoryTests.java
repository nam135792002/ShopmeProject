package com.shopme.admin.setting.country;

import com.shopme.common.entity.Country;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CountryRepositoryTests {
    @Autowired private CountryRepository countryRepository;

    @Test
    public void testCreateCountry(){
//        Country country = countryRepository.save(new Country("China","CN"));
        Iterable<Country> country = countryRepository.saveAll(List.of(new Country("Vietnam","VN"),
                                                            new Country("Republic of Korean","KO"),
                                                            new Country("United Kingdom","UK"),
                                                            new Country("United States American","USA")));
    }

    @Test
    public void testListCountries(){
        List<Country> listCountries = countryRepository.findAllByOrderByNameAsc();
        listCountries.forEach(System.out::println);
        Assertions.assertThat(listCountries.size()).isGreaterThan(0);
    }

    @Test
    public void testUpdateCountry(){
        Integer id = 1;
        String name = "Republic of India";

        Country country = countryRepository.findById(id).get();
        country.setName(name);
        Country updatedCountry = countryRepository.save(country);
        Assertions.assertThat(updatedCountry.getName()).isEqualTo(name);
    }

    @Test
    public void testgetCountry(){
        Integer id = 3;
        Country country = countryRepository.findById(id).get();
        Assertions.assertThat(country).isNotNull();
    }

    @Test
    public void testDeleteCountry(){
        Integer id = 5;
        countryRepository.deleteById(id);

        Optional<Country> findById = countryRepository.findById(id);
        Assertions.assertThat(findById).isEmpty();
    }
}
