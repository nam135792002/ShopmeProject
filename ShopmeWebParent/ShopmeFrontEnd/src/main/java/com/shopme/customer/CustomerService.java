package com.shopme.customer;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.setting.CountryRepository;
import jakarta.transaction.Transactional;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CustomerService {

    @Autowired private CountryRepository countryRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired PasswordEncoder passwordEncoder;

    public List<Country> listAllCountries(){
        return countryRepository.findAllByOrderByNameAsc();
    }

    public boolean isEmailUnique(String email){
        Customer customer = customerRepository.findByEmail(email);
        return customer == null;
    }

    public void registerCustomer(Customer customer){
        encodePassword(customer);
        customer.setEnabled(false);
        customer.setCreatedTime(new Date());
        customer.setAuthenticationType(AuthenticationType.DATABASE);

        String randomCode = RandomString.make(64);
        customer.setVerificationCode(randomCode);
        customerRepository.save(customer);
    }

    private void encodePassword(Customer customer) {
        String encodedPassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encodedPassword);
    }

    public boolean verify(String verificationCode){
        Customer customer = customerRepository.findByVerificationCode(verificationCode);

        if(customer == null || customer.isEnabled()){
            return false;
        }else{
            customerRepository.enable(customer.getId());
            return true;
        }
    }

    public void updateAuthenticationType(Customer customer, AuthenticationType type){
        if(!customer.getAuthenticationType().equals(type)){
            customerRepository.updateAuthenticationType(customer.getId(), type);
        }
    }

    public Customer getCustomerByEmail(String email){
        return customerRepository.findByEmail(email);
    }

    public void addNewCustomerUponOAuthLogin(String name, String email, String countryCode) {
        Customer customer = new Customer();
        customer.setEmail(email);
        setName(name,customer);
        customer.setEnabled(true);
        customer.setCreatedTime(new Date());
        customer.setAuthenticationType(AuthenticationType.GOOGLE);
        customer.setPassword("");
        customer.setAddressLine1("");
        customer.setAddressLine2("");
        customer.setCity("");
        customer.setCountry(countryRepository.findByCode(countryCode));
        customer.setState("");
        customer.setPhoneNumber("");
        customer.setPostalCode("");
        customerRepository.save(customer);
    }

    private void setName(String name, Customer customer){
        String[] nameArray = name.split(" ");
        if(nameArray.length < 2){
            customer.setFirstName(name);
            customer.setLastName("");
        }else{
            String firstName = nameArray[0];
            customer.setFirstName(firstName);

            String lastName = name.replaceFirst(firstName,"");
            customer.setLastName(lastName);
        }
    }

    public String updateResetPasswordToken(String email) throws CustomerNotFoundException {
        Customer customer = customerRepository.findByEmail(email);
        if(customer != null){
            String token = RandomString.make(30);
            customer.setResetPasswordToken(token);
            customerRepository.save(customer);
            return token;
        }else{
            throw new CustomerNotFoundException("Could not find any customer with the email " + email);
        }
    }

    public Customer getByResetPasswordToken(String token){
        return customerRepository.findByResetPasswordToken(token);
    }

    public void updatePassword(String token, String newPassword) throws CustomerNotFoundException {
        Customer customer = customerRepository.findByResetPasswordToken(token);
        if(customer == null){
            throw new CustomerNotFoundException("No customer found: invalid token");
        }
        customer.setPassword(newPassword);
        customer.setResetPasswordToken(null);
        encodePassword(customer);
        customerRepository.save(customer);
    }
}
