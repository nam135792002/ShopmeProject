package com.shopme.admin.customer;

import com.shopme.admin.paging.SearchRepository;
import com.shopme.common.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends SearchRepository<Customer, Integer> {

    @Query("select c from Customer c where concat(c.email,' ',c.firstName,' ',c.lastName,' '," +
            "c.addressLine1,' ',c.addressLine2,' ',c.city,' ',c.state,' '," +
            "c.postalCode,' ',c.country.name) like %?1%")
    public Page<Customer> findAll(String keyword, Pageable pageable);

    @Query("select c from Customer  c where c.email = ?1")
    public Customer findByEmail(String email);

    public Long countById(Integer id);

    @Query("update Customer  c set c.enabled = ?2 where c.id = ?1")
    @Modifying
    public void updateEnabledStatus(Integer id, boolean enabled);
}
