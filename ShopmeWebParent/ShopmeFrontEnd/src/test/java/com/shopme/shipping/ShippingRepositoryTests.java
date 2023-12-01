package com.shopme.shipping;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRates;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShippingRepositoryTests {
    @Autowired private ShippingRateRepository shippingRateRepository;

    @Test
    public void testFindByCountryAndState(){
        Country vn = new Country(2);
        String state = "Binh Dinh";
        ShippingRates findByCountryAndState = shippingRateRepository.findByCountryAndState(vn,state);
        Assertions.assertThat(findByCountryAndState).isNotNull();
        System.out.println(findByCountryAndState);
    }
}
