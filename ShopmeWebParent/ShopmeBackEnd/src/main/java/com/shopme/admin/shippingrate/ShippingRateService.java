package com.shopme.admin.shippingrate;

import com.shopme.admin.paging.PagingAndSortingHeper;
import com.shopme.admin.product.ProductRepository;
import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.ShippingRates;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@Transactional
public class ShippingRateService {
    public static final int RATES_PER_PAGE = 4;
    public static final int DIM_DIVISOR = 139;

    @Autowired private ShippingRateRepository shippingRateRepository;
    @Autowired private CountryRepository countryRepository;
    @Autowired private ProductRepository productRepository;

    public void listByPage(int pageNum, PagingAndSortingHeper heper){
        heper.listEntities(pageNum, RATES_PER_PAGE, shippingRateRepository);
    }

    public List<Country> listAllCountries(){
        return countryRepository.findAllByOrderByNameAsc();
    }

    public void save(ShippingRates rateInform) throws ShippingRateAlreadyExistsException {
        ShippingRates rateInDB = shippingRateRepository.findByCountryAndState(rateInform.getCountry().getId(), rateInform.getState());
        boolean foundExistingRateInNewMode = rateInform.getId() == null && rateInDB != null;
        boolean foundDifferenceExistingRateInEditMode = rateInform.getId() != null && rateInDB != null && !Objects.equals(rateInDB.getId(), rateInform.getId());

        if(foundExistingRateInNewMode || foundDifferenceExistingRateInEditMode){
            throw new ShippingRateAlreadyExistsException("There's already a rate for the destination " + rateInform.getCountry().getName() + ", " + rateInform.getState());
        }
        shippingRateRepository.save(rateInform);
    }

    public ShippingRates get(Integer id) throws ShippingrateNotFoundException {
        try {
            return shippingRateRepository.findById(id).get();
        }catch (NoSuchElementException ex){
            throw new ShippingrateNotFoundException("Could not find shipping rate with ID " + id);
        }
    }

    public void updateCODSupport(Integer id, boolean codSupported) throws ShippingrateNotFoundException {
        Long count = shippingRateRepository.countById(id);
        if(count == null || count == 0){
            throw new ShippingrateNotFoundException("Could not find shipping rate with ID " + id);
        }
        shippingRateRepository.updateCODSupport(id,codSupported);
    }

    public void delete(Integer id) throws ShippingrateNotFoundException {
        Long count = shippingRateRepository.countById(id);
        if(count == null || count == 0){
            throw new ShippingrateNotFoundException("Could not find shipping rate with ID "+ id);
        }
        shippingRateRepository.deleteById(id);
    }

    public float calculateShippingCost(Integer productId, Integer countryId, String state) throws ShippingrateNotFoundException {
        ShippingRates shippingRates = shippingRateRepository.findByCountryAndState(countryId, state);
        if(shippingRates == null){
            throw new ShippingrateNotFoundException("No shipping rate found for the given destination. You have to enter shipping cost manually.");
        }

        Product product = productRepository.findById(productId).get();
        float dimWeight = (product.getLength() * product.getWidth() * product.getHeight()) / DIM_DIVISOR;
        float finalWeight = product.getWeight() > dimWeight ? product.getWeight() : dimWeight;

        return finalWeight;
    }
}
