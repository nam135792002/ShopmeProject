package com.shopme.address;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    public List<Address> findByCustomer(Customer customer);

    @Query("select a from Address a where a.id = ?1 and a.customer.id = ?2")
    public Address findByIdAndCustomer(Integer addressId, Integer customerId);

    @Query("delete from Address a where a.id = ?1 and a.customer.id = ?2")
    @Modifying
    public void deleteByIdAndCustomer(Integer addressId, Integer customerId);

    @Query("update Address a set a.defaultForShipping = true where a.id = ?1")
    @Modifying
    public void setDefaultAddress(Integer id);

    @Query("update Address a set a.defaultForShipping = false where a.id != ?1 and a.customer.id = ?2")
    @Modifying
    public void setNonDefaultForOthers(Integer defaultAddressId, Integer customerId);

    @Query("select a from Address a where a.customer.id = ?1 and a.defaultForShipping = true")
    public Address findDefaultCustomer(Integer customerId);
}
