package com.shopme.address;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class AddressRepositoryTests {
    @Autowired private AddressRepository repository;

    @Test
    public void testAddNew(){
        Integer customerId = 2;
        Integer countryId = 2;

        Address newAddress = new Address();
        newAddress.setCustomer(new Customer(customerId));
        newAddress.setCountry(new Country(countryId));
        newAddress.setFirstName("Nam");
        newAddress.setLastName("Ri");
        newAddress.setPhoneNumber("0334069054");
        newAddress.setAddressLine1("02 Vo Oanh St 25 Ward");
        newAddress.setAddressLine2("01 Vo Van Ngan Hiep Binh Ward");
        newAddress.setCity("Binh Thanh");
        newAddress.setState("Ho Chi Minh");
        newAddress.setPostalCode("10013");

        Address savedAddress = repository.save(newAddress);
        Assertions.assertThat(savedAddress).isNotNull();
        Assertions.assertThat(savedAddress.getId()).isGreaterThan(0);
    }

    @Test
    public void testFindByCustomer(){
        Integer customerId = 2;
        List<Address> listAddresses = repository.findByCustomer(new Customer(customerId));
        Assertions.assertThat(listAddresses.size()).isGreaterThan(0);

        listAddresses.forEach(System.out::println);
    }

    @Test
    public void testFindByIdAndCustomer(){
        Integer addressId = 1;
        Integer customerId = 2;

        Address address = repository.findByIdAndCustomer(addressId,customerId);

        Assertions.assertThat(address).isNotNull();
        System.out.println(address);
    }

    @Test
    public void testUpdate(){
        Integer addressId = 2;
//        String phoneNUmber = "0934958717";
        Address address = repository.findById(addressId).get();
//        address.setPhoneNumber(phoneNUmber);
        address.setDefaultForShipping(true);
        Address updatedAddress = repository.save(address);
        Assertions.assertThat(updatedAddress.isDefaultForShipping()).isTrue();
    }

    @Test
    public void testDeleteByIdAndCustomer(){
        Integer addressId = 1;
        Integer customerId = 2;
        repository.deleteByIdAndCustomer(addressId,customerId);

        Address address = repository.findByIdAndCustomer(addressId,customerId);
        Assertions.assertThat(address).isNull();
    }
}
