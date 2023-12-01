package com.shopme.shoppingcart;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CartItemRepositoryTests {
    @Autowired private CartItemRepository repository;
    @Autowired private TestEntityManager entityManager;

    @Test
    public void testSaveItem1(){
        Integer customerId = 2;
        Integer productId = 1;

        Customer customer = entityManager.find(Customer.class,customerId);
        Product product = entityManager.find(Product.class, productId);

        CartItem cartItem = new CartItem();
        cartItem.setCustomer(customer);
        cartItem.setProduct(product);
        cartItem.setQuantity(2);

        CartItem savedItem = repository.save(cartItem);
        Assertions.assertThat(savedItem.getId()).isGreaterThan(0);
    }

    @Test
    public void testSaveItem2(){
        Integer customerId = 26;
        Integer productId = 3;

        Customer customer = entityManager.find(Customer.class,customerId);
        Product product = entityManager.find(Product.class, productId);

        CartItem cartItem1 = new CartItem();
        cartItem1.setCustomer(customer);
        cartItem1.setProduct(product);
        cartItem1.setQuantity(2);

        CartItem cartItem2 = new CartItem();
        cartItem2.setCustomer(new Customer(customerId));
        cartItem2.setProduct(new Product(8));
        cartItem2.setQuantity(3);

        Iterable<CartItem> iterable = repository.saveAll(List.of(cartItem1,cartItem2));
        Assertions.assertThat(iterable).size().isGreaterThan(0);
    }

    @Test
    public void testFindByCustomer(){
        Integer customerId = 26;
        List<CartItem> listItems = repository.findByCustomer(new Customer(customerId));
        listItems.forEach(System.out::println);
        Assertions.assertThat(listItems.size()).isEqualTo(2);
    }

    @Test
    public void testFindByCustomerAndProduct(){
        Integer customerId = 2;
        Integer productId = 1;

        CartItem item = repository.findByCustomerAndProduct(new Customer(customerId), new Product(productId));

        Assertions.assertThat(item).isNotNull();
        System.out.println(item);
    }

    @Test
    public void testUpdateQuantity(){
        Integer customerId = 2;
        Integer productId = 1;
        Integer quantity = 4;

        repository.updateQuantity(quantity,customerId,productId);

        CartItem item = repository.findByCustomerAndProduct(new Customer(customerId), new Product(productId));

        Assertions.assertThat(item.getQuantity()).isEqualTo(4);
    }

    @Test
    public void testDeleteByCustomerAndProduct(){
        Integer customerId = 2;
        Integer productId = 1;

        repository.deleteByCustomerAndProduct(customerId,productId);

        CartItem item = repository.findByCustomerAndProduct(new Customer(customerId), new Product(productId));

        Assertions.assertThat(item).isNull();
    }
}
