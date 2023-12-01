package com.shopme.admin.product;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.ProductImage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void testCreateProduct(){
        Brand brand = testEntityManager.find(Brand.class, 37);
        Category category = testEntityManager.find(Category.class, 5);

        Product product = new Product();
        product.setName("Acer Aspire Desktop");
        product.setAlias("acer_aspire_desktop");
        product.setShortDescription("Sort description for Acer Aspire");
        product.setFullDescription("Full description for Acer Aspire");

        product.setBrand(brand);
        product.setCategory(category);

        product.setPrice(456);
        product.setCost(400);
        product.setEnabled(true);
        product.setInStock(true);
        product.setCreatedTime(new Date());
        product.setUpdatedTime(new Date());

        Product savedProduct = productRepository.save(product);
        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isGreaterThan(0);

    }

    @Test
    public void testListAllProducts(){
        Iterable<Product> iterableProducts = productRepository.findAll();
        iterableProducts.forEach(System.out::println);
    }

    @Test
    public void testGetProduct(){
        Integer id = 2;
        Product product = productRepository.findById(id).get();
        System.out.println(product);
        assertThat(product).isNotNull();
    }

    @Test
    public void testUpdateProduct(){
        Integer id = 1;
        Product product = productRepository.findById(id).get();
        product.setPrice(499);
        productRepository.save(product);
        Product updatedProduct = testEntityManager.find(Product.class,id);
        assertThat(updatedProduct.getPrice()).isEqualTo(499);
    }

    @Test
    public void testDeleteProduct(){
        Integer id = 3;
        productRepository.deleteById(id);

        Optional<Product> result = productRepository.findById(id);
        assertThat(!result.isPresent());
    }

    @Test
    public void testSaveProductWithImages(){
        Integer id = 1;
        Product product = productRepository.findById(id).get();

        product.setMainImage("main image.png");
        product.addExtraImage("extra image 1.png","abc");
        product.addExtraImage("extra image 2.png","abc");
        product.addExtraImage("extra image 3.png","abc");
        Product savedProduct = productRepository.save(product);

        assertThat(savedProduct.getImages().size()).isEqualTo(3);
    }

    @Test
    public void testImageExtra(){
        Integer id = 1;
        List<ProductImage> list = productRepository.findExtraImageByProduct(id);
        for (ProductImage image : list){
            System.out.println(image.getName());
        }
        assertThat(list.size()).isEqualTo(3);
    }

    @Test
    public void testCountImageExtra(){
        Integer id = 1;
        long count = productRepository.countImageExtraById(1);
        System.out.println(count);
        assertThat(count).isEqualTo(3);
    }

    @Test
    public void testSaveProductWithDetails(){
        Integer productId = 1;
        Product product = productRepository.findById(productId).get();

        product.addDetail("Device Memory","128 GB");
        product.addDetail("CPU Model","MediaTek");
        product.addDetail("OS","Android 10");

        Product savedProduct = productRepository.save(product);
        assertThat(savedProduct.getDetails()).isNotEmpty();
    }

    @Test
    public void testSaveProductWithDescription(){
        Integer productId = 18;
        Product product = productRepository.findById(productId).get();

        product.setShortDescription("12.9-inch (6th Generation): with M2 chip, Liquid Retina XDR Display, 256GB, Wi-Fi 6E, 12MP front/12MP and 10MP Back Cameras, Face ID, All-Day Battery Life – Space Gray");
        product.setFullDescription("WHY IPAD PRO — iPad Pro is the ultimate iPad experience, with the astonishing performance of the M2 chip, superfast wireless connectivity, and next-generation Apple Pencil experience. Plus powerful productivity features in iPadOS.\n" +
                "IPADOS + APPS — iPadOS makes iPad more productive, intuitive, and versatile. With iPadOS, run multiple apps at once, use Apple Pencil to write in any text field with Scribble, and edit and share photos. Stage Manager makes multitasking easy with resizable, overlapping apps and external display support. iPad Pro comes with essential apps like Safari, Messages, and Keynote, with over a million more apps available on the App Store.\n" +
                "FAST WI-FI CONNECTIVITY — Wi-Fi 6E gives you fast wireless connections. Work from almost anywhere with quick transfers of photos, documents, and large video files.");
        Product savedProduct = productRepository.save(product);
        assertThat(savedProduct.getShortDescription()).isNotEmpty();
        assertThat(savedProduct.getFullDescription()).isNotEmpty();
    }


}
