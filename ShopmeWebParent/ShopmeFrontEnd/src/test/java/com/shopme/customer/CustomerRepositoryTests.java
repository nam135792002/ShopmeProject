package com.shopme.customer;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import jakarta.servlet.http.HttpServletRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.Locale;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CustomerRepositoryTests {
    @Autowired private CustomerRepository customerRepository;
    @Autowired private TestEntityManager entityManager;

    @Test
    public void testCreateCustomer1(){
        Integer countryId = 2;
        Country country = entityManager.find(Country.class,countryId);

        Customer customer = new Customer();

        customer.setCountry(country);
        customer.setFirstName("Nam");
        customer.setLastName("Nguyen Phuong");
        customer.setPassword("123456789");
        customer.setEmail("21h1120046@ut.edu.vn");
        customer.setPhoneNumber("0334069054");
        customer.setAddressLine1("03 An Thai 05");
        customer.setCity("An Nhon");
        customer.setState("Binh Dinh");
        customer.setPostalCode("95867");
        customer.setCreatedTime(new Date());

        Customer savedCustomer = customerRepository.save(customer);

        Assertions.assertThat(savedCustomer).isNotNull();
        Assertions.assertThat(savedCustomer.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateCustomer2(){
        Integer countryId = 2;
        Country country = entityManager.find(Country.class,countryId);

        Customer customer = new Customer();
        customer.setCountry(country);
        customer.setFirstName("Thanh");
        customer.setLastName("Le Tu");
        customer.setPassword("123456789");
        customer.setEmail("thanhking2k2@gmail.com");
        customer.setPhoneNumber("0934958717");
        customer.setAddressLine1("36/44/06 Nguyen Gia Tri");
        customer.setAddressLine2("02 Vo Oanh");
        customer.setCity("Binh Thanh");
        customer.setState("Ho Chi Minh");
        customer.setPostalCode("400013");
        customer.setCreatedTime(new Date());

        Customer savedCustomer = customerRepository.save(customer);

        Assertions.assertThat(savedCustomer).isNotNull();
        Assertions.assertThat(savedCustomer.getId()).isGreaterThan(0);
    }

    @Test
    public void testListCustomers(){
        Iterable<Customer> customers = customerRepository.findAll();
        customers.forEach(System.out::println);
        Assertions.assertThat(customers).hasSizeGreaterThan(1);
    }
    
    @Test
    public void testUpdateCustomer(){
        Integer customerId = 1;
        String lastName = "Nguyen Phuong Thien";

        Customer customer = customerRepository.findById(customerId).get();
        customer.setLastName(lastName);
        customer.setEnabled(true);

        Customer updatedCustomer = customerRepository.save(customer);
        Assertions.assertThat(updatedCustomer.getLastName()).isEqualTo(lastName);
    }

    @Test
    public void testGetCustomer(){
        Integer customerId = 2;
        Optional<Customer> findById = customerRepository.findById(customerId);

        Assertions.assertThat(findById).isPresent();

        Customer customer = findById.get();
        System.out.println(customer);
    }

    @Test
    public void testDeleteCustomer(){
        Integer customerId = 2;
        customerRepository.deleteById(customerId);
        Optional<Customer> findById = customerRepository.findById(customerId);
        Assertions.assertThat(findById).isNotPresent();
    }

    @Test
    public void testFindByEmail(){
        String email = "21h1120046@ut.edu.vn";
        Customer customer = customerRepository.findByEmail(email);

        Assertions.assertThat(customer).isNotNull();
        System.out.println(customer);
    }

    @Test
    public void testFindByVerificationCode(){
        String code = "code_123";
        Customer customer = customerRepository.findByVerificationCode(code);

        Assertions.assertThat(customer).isNotNull();
        System.out.println(customer);
    }

    @Test
    public void testEnableCustomer(){
        Integer customerId = 1;
        customerRepository.enable(customerId);

        Customer customer = customerRepository.findById(customerId).get();
        Assertions.assertThat(customer.isEnabled()).isTrue();
    }

    @Test
    public void testUpdateAuthenticationType(){
        Integer id = 1;
        customerRepository.updateAuthenticationType(id, AuthenticationType.FACEBOOK);

        Customer customer = customerRepository.findById(id).get();
        Assertions.assertThat(customer.getAuthenticationType()).isEqualTo(AuthenticationType.FACEBOOK);
    }

}
