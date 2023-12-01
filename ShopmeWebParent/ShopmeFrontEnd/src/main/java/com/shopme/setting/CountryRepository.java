package com.shopme.setting;

import com.shopme.common.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CountryRepository extends JpaRepository<Country, Integer> {
    public List<Country> findAllByOrderByNameAsc();

    @Query("select c from Country c where c.code = ?1")
    public Country findByCode(String code);
}
