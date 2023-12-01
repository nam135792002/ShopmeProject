package com.shopme.admin.review;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.Review;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Date;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class ReviewRepositoryTests {
    @Autowired private ReviewRepository repository;

    @Test
    public void testCreateReview(){
        Integer productId = 2;
        Product product = new Product(productId);

        Integer customerId = 2;
        Customer customer = new Customer(customerId);

        Review review = new Review();
        review.setProduct(product);
        review.setCustomer(customer);
        review.setReviewTime(new Date());
        review.setHeadline("Perfect for my needs. Loving it!");
        review.setComment("Nice to have: wireless remote, ios app, GPS...");
        review.setRating(5);

        Review savedReview = repository.save(review);

        Assertions.assertThat(savedReview).isNotNull();
        Assertions.assertThat(savedReview.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateReview1(){
        Integer productId = 123;
        Product product = new Product(productId);

        Integer customerId = 1;
        Customer customer = new Customer(customerId);

        Review review = new Review();
        review.setProduct(product);
        review.setCustomer(customer);
        review.setReviewTime(new Date());
        review.setHeadline("Greating! Lighting Cable");
        review.setComment("I decided to give it a try, and I am very pleased with it.");
        review.setRating(5);

        Review savedReview = repository.save(review);

        Assertions.assertThat(savedReview).isNotNull();
        Assertions.assertThat(savedReview.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateReview2(){
        Integer productId = 64;
        Product product = new Product(productId);

        Integer customerId = 4;
        Customer customer = new Customer(customerId);

        Review review = new Review();
        review.setProduct(product);
        review.setCustomer(customer);
        review.setReviewTime(new Date());
        review.setHeadline("Nice looking and cheap");
        review.setComment("This is the 3rd time I have ordered these. They don't last that I...");
        review.setRating(3);

        Review savedReview = repository.save(review);

        Assertions.assertThat(savedReview).isNotNull();
        Assertions.assertThat(savedReview.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateReview3(){
        Integer productId = 75;
        Product product = new Product(productId);

        Integer customerId = 5;
        Customer customer = new Customer(customerId);

        Review review = new Review();
        review.setProduct(product);
        review.setCustomer(customer);
        review.setReviewTime(new Date());
        review.setHeadline("Not bad");
        review.setComment("I'm changing my 2 star to four star, left a 2 star because the title cou...");
        review.setRating(4);

        Review savedReview = repository.save(review);

        Assertions.assertThat(savedReview).isNotNull();
        Assertions.assertThat(savedReview.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateReview4(){
        Integer productId = 117;
        Product product = new Product(productId);

        Integer customerId = 6;
        Customer customer = new Customer(customerId);

        Review review = new Review();
        review.setProduct(product);
        review.setCustomer(customer);
        review.setReviewTime(new Date());
        review.setHeadline("Good value if you value your time. Perfect balance be...");
        review.setComment("This is a very good quantity tripod for most users. Another review...");
        review.setRating(5);

        Review savedReview = repository.save(review);

        Assertions.assertThat(savedReview).isNotNull();
        Assertions.assertThat(savedReview.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateReview5(){
        Integer productId = 48;
        Product product = new Product(productId);

        Integer customerId = 8;
        Customer customer = new Customer(customerId);

        Review review = new Review();
        review.setProduct(product);
        review.setCustomer(customer);
        review.setReviewTime(new Date());
        review.setHeadline("Great so far. Hoping for longvity.");
        review.setComment("I'm digging them so far. I purcharsed this two pack so that I could...");
        review.setRating(5);

        Review savedReview = repository.save(review);

        Assertions.assertThat(savedReview).isNotNull();
        Assertions.assertThat(savedReview.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateReview6(){
        Integer productId = 54;
        Product product = new Product(productId);

        Integer customerId = 10;
        Customer customer = new Customer(customerId);

        Review review = new Review();
        review.setProduct(product);
        review.setCustomer(customer);
        review.setReviewTime(new Date());
        review.setHeadline("Good card and a good value.");
        review.setComment("I have always had good luck with ...");
        review.setRating(5);

        Review savedReview = repository.save(review);

        Assertions.assertThat(savedReview).isNotNull();
        Assertions.assertThat(savedReview.getId()).isGreaterThan(0);
    }
}
