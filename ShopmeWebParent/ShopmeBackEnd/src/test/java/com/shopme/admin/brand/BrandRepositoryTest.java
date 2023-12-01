package com.shopme.admin.brand;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class BrandRepositoryTest {
    @Autowired
    private BrandRepository brandRepository;

    @Test
    public void testCreateBrand(){
        Category laptops = new Category(6);
        Brand acer = new Brand("Acer");

        acer.getCategories().add(laptops);
        Brand savedAcer = brandRepository.save(acer);
        assertThat(savedAcer).isNotNull();
        assertThat(savedAcer.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateTwoBrand(){
        Category cellphones = new Category(4);
        Category tablets = new Category(7);
        Brand apple = new Brand("Apple");

        apple.getCategories().add(cellphones);
        apple.getCategories().add(tablets);
        Brand savedApple = brandRepository.save(apple);
        assertThat(savedApple).isNotNull();
        assertThat(savedApple.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateTwoBrandCont(){
        Brand samsung = new Brand("Samsung");

        samsung.getCategories().add(new Category(29));
        samsung.getCategories().add(new Category(24));
        Brand savedSamsung = brandRepository.save(samsung);

        assertThat(savedSamsung).isNotNull();
        assertThat(savedSamsung.getId()).isGreaterThan(0);
    }

    @Test
    public void testFindAllBrand(){
        List<Brand> listBrands = brandRepository.findAll();
        for(Brand brand : listBrands){
            System.out.println(brand);
        }
        assertThat(listBrands).isNotEmpty();
    }

    @Test
    public void testGetBrand(){
        Brand brand = brandRepository.findById(1).get();
        System.out.println(brand);

        assertThat(brand.getName()).isEqualTo("Acer");
    }

    @Test
    public void testUpdateBrand(){
        Brand brand = brandRepository.findById(3).get();
        String newName = "Samsung Electronics";

        brand.setName(newName);
        Brand savedBrand = brandRepository.save(brand);
        assertThat(savedBrand.getName()).isEqualTo(newName);

    }

    @Test void testDeleteBrand(){
        Integer id = 2;
        brandRepository.deleteById(id);

        Brand brand = brandRepository.findById(id).get();
        assertThat(brand).isNull();
    }
}
