package com.shopme.admin.customer;

import com.shopme.admin.paging.PagingAndSortingHeper;
import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class CustomerService {

    public static final int CUSTOMER_PER_PAGE = 4;

    @Autowired private CustomerRepository customerRepository;
    @Autowired private CountryRepository countryRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    public void listByPage(int pageNum, PagingAndSortingHeper helper){
        helper.listEntities(pageNum,CUSTOMER_PER_PAGE,customerRepository);
    }

    public void updateCustomerEnabledStatus(Integer id, boolean enabled){
        customerRepository.updateEnabledStatus(id,enabled);
    }

    public Customer get(Integer id) throws CustomerNotFoundException {
        try {
            return customerRepository.findById(id).get();
        }catch (NoSuchElementException ex){
            throw new CustomerNotFoundException("Could not find any customers with ID " + id);
        }
    }

    public List<Country> listAllCountries(){
        return countryRepository.findAllByOrderByNameAsc();
    }

    public boolean isEmailUnique(Integer id, String email){
        Customer existCustomer = customerRepository.findByEmail(email);
        if(existCustomer != null && existCustomer.getId() != id){
            return false;
        }
        return true;
    }

    public void save(Customer customerInform){
        Customer customerInDB = customerRepository.findById(customerInform.getId()).get();
        if(!customerInform.getPassword().isEmpty()){
            String encodedPassword = passwordEncoder.encode(customerInform.getPassword());
            customerInform.setPassword(encodedPassword);
        } else {
            customerInform.setPassword(customerInDB.getPassword());
        }
        customerInform.setEnabled(customerInDB.isEnabled());
        customerInform.setCreatedTime(customerInDB.getCreatedTime());
        customerInform.setVerificationCode(customerInDB.getVerificationCode());
        customerInform.setResetPasswordToken(customerInDB.getResetPasswordToken());
        customerRepository.save(customerInform);
    }

    public void delete(Integer id) throws CustomerNotFoundException {
        Long count = customerRepository.countById(id);
        if(count == null || count == 0){
            throw new CustomerNotFoundException("Could not find any customer with ID " + id);
        }
        customerRepository.deleteById(id);
    }
}
