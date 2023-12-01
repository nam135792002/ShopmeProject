package com.shopme.admin.shippingrate;

import com.shopme.admin.paging.SearchRepository;
import com.shopme.common.entity.ShippingRates;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ShippingRateRepository extends SearchRepository<ShippingRates, Integer> {

    @Query("select sr from ShippingRates sr where sr.country.id = ?1 and sr.state = ?2")
    public ShippingRates findByCountryAndState(Integer countryId, String state);

    @Query("update ShippingRates sr set sr.codSupported = ?2 where sr.id = ?1")
    @Modifying
    public void updateCODSupport(Integer id, boolean enabled);

    @Query("select sr from ShippingRates sr where sr.country.name like %?1% or sr.state like %?1%")
    public Page<ShippingRates> findAll(String keyword, Pageable pageable);

    public Long countById(Integer id);
}
