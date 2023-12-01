package com.shopme.admin.order;

import com.shopme.admin.report.MasterOrderReportService;
import com.shopme.admin.report.ReportItem;
import com.shopme.common.entity.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class OrderRepositoryTests {
    @Autowired private OrderRepository repository;
    @Autowired private TestEntityManager entityManager;
    @Autowired private OrderDetailRepository orderDetailRepository;
    @Autowired private MasterOrderReportService service;

    @Test
    public void testCreateNewOrderWithSingleProduct(){
        Customer customer = entityManager.find(Customer.class, 2);
        Product product = entityManager.find(Product.class, 1);
        Order mainOrder = new Order();

        mainOrder.setOrderTime(new Date());
        mainOrder.setCustomer(customer);
        mainOrder.copyAddressFromCustomer();

        mainOrder.setShippingCost(10);
        mainOrder.setProductCost(product.getCost());
        mainOrder.setTax(0);
        mainOrder.setSubtotal(product.getPrice());
        mainOrder.setTotal(product.getPrice() + 10);

        mainOrder.setPaymentMethod(PaymentMethod.VNPay);
        mainOrder.setStatus(OrderStatus.NEW);
        mainOrder.setDeliverDate(new Date());
        mainOrder.setDeliverDays(1);

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setProduct(product);
        orderDetail.setOrder(mainOrder);
        orderDetail.setProductCost(product.getCost());
        orderDetail.setShippingCost(10);
        orderDetail.setQuantity(1);
        orderDetail.setSubtotal(product.getPrice());
        orderDetail.setUnitPrice(product.getPrice());

        mainOrder.getOrderDetails().add(orderDetail);

        Order savedOrder = repository.save(mainOrder);

        Assertions.assertThat(savedOrder.getId()).isGreaterThan(0);

    }

    @Test
    public void testCreateNewOrderWithMultipleProduct(){
        Customer customer = entityManager.find(Customer.class, 4);
        Product product1 = entityManager.find(Product.class, 2);
        Product product2 = entityManager.find(Product.class,3);
        Order mainOrder = new Order();

        mainOrder.setOrderTime(new Date());
        mainOrder.setCustomer(customer);
        mainOrder.copyAddressFromCustomer();

        OrderDetail orderDetail1 = new OrderDetail();
        orderDetail1.setProduct(product1);
        orderDetail1.setOrder(mainOrder);
        orderDetail1.setProductCost(product1.getCost());
        orderDetail1.setShippingCost(10);
        orderDetail1.setQuantity(1);
        orderDetail1.setSubtotal(product1.getPrice());
        orderDetail1.setUnitPrice(product1.getPrice());

        OrderDetail orderDetail2 = new OrderDetail();
        orderDetail2.setProduct(product2);
        orderDetail2.setOrder(mainOrder);
        orderDetail2.setProductCost(product2.getCost());
        orderDetail2.setShippingCost(10);
        orderDetail2.setQuantity(1);
        orderDetail2.setSubtotal(product2.getPrice());
        orderDetail2.setUnitPrice(product2.getPrice());

        mainOrder.getOrderDetails().add(orderDetail1);
        mainOrder.getOrderDetails().add(orderDetail2);

        mainOrder.setShippingCost(30);
        mainOrder.setProductCost(product1.getCost() + product2.getCost());
        mainOrder.setTax(0);
        float subtotal = product1.getPrice() + product2.getPrice();
        mainOrder.setSubtotal(subtotal);
        mainOrder.setTotal(subtotal + 30);

        mainOrder.setPaymentMethod(PaymentMethod.COD);
        mainOrder.setStatus(OrderStatus.PROCESSING);
        mainOrder.setDeliverDate(new Date());
        mainOrder.setDeliverDays(3);

        Order savedOrder = repository.save(mainOrder);

        Assertions.assertThat(savedOrder.getId()).isGreaterThan(0);
    }

    @Test
    public void testListOrders(){
        Iterable<Order> orders = repository.findAll();
        Assertions.assertThat(orders).hasSizeGreaterThan(0);
        orders.forEach(System.out::println);

    }

    @Test
    public void testUpdateOrder(){
        Integer orderId = 2;
        Order order = repository.findById(orderId).get();

        order.setStatus(OrderStatus.PACKAGED);
        order.setPaymentMethod(PaymentMethod.COD);
        order.setOrderTime(new Date());
        order.setDeliverDays(5);
        Order updatedOrder = repository.save(order);

        Assertions.assertThat(updatedOrder.getStatus()).isEqualTo(OrderStatus.PACKAGED);
    }

    @Test
    public void testGetOrder(){
        Integer orderId = 2;
        Order order = repository.findById(orderId).get();

        Assertions.assertThat(order).isNotNull();
        System.out.println(order);
    }

    @Test
    public void testDeleteOrder(){
        Integer orderId = 3;
        repository.deleteById(orderId);

        Optional<Order> order = repository.findById(orderId);
        Assertions.assertThat(order).isNotPresent();
    }

    @Test
    public void testUpdatedOrderTracks(){
        Integer orderId = 13;
        Order order = repository.findById(orderId).get();

        OrderTrack newTrack = new OrderTrack();
        newTrack.setOrder(order);
        newTrack.setUpdatedTime(new Date());
        newTrack.setStatus(OrderStatus.PICKED);
        newTrack.setNotes(OrderStatus.PICKED.defaultDescription());

        OrderTrack newTrack2 = new OrderTrack();
        newTrack2.setOrder(order);
        newTrack2.setUpdatedTime(new Date());
        newTrack2.setStatus(OrderStatus.PACKAGED);
        newTrack2.setNotes(OrderStatus.PACKAGED.defaultDescription());

        List<OrderTrack> orderTracks = order.getOrderTracks();
        orderTracks.add(newTrack);
        orderTracks.add(newTrack2);

        Order updatedOrder = repository.save(order);

        Assertions.assertThat(updatedOrder.getOrderTracks()).hasSizeGreaterThan(1);
    }

    @Test
    public void testFindByOrderTimeBetween() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startTime = dateFormat.parse("2023-10-01");
        Date endTime = dateFormat.parse("2023-11-01");

        List<Order> listOrders = repository.findByOrderTimeBetween(startTime,endTime);

        Assertions.assertThat(listOrders.size()).isGreaterThan(0);

        for (Order order : listOrders){
            System.out.printf("%s | %s | %.2f | %.2f | %.2f \n", order.getId(), order.getOrderTime(), order.getSubtotal(), order.getProductCost(), order.getTotal());
        }
    }

    @Test
    public void testFindWithCategoryAndtimeBetween() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startTime = dateFormat.parse("2023-10-01");
        Date endTime = dateFormat.parse("2023-11-01");

        List<OrderDetail> listOrderDetails = orderDetailRepository.findWithCategoryAndTimeBetween(startTime,endTime);

        Assertions.assertThat(listOrderDetails.size()).isGreaterThan(0);

        for (OrderDetail orderDetail : listOrderDetails){
            System.out.printf("%s | %d | %.2f | %.2f | %.2f \n", orderDetail.getProduct().getCategory().getName(), orderDetail.getQuantity(), orderDetail.getSubtotal(), orderDetail.getProductCost(), orderDetail.getShippingCost());
        }
    }

    @Test
    public void testFindWithProductAndtimeBetween() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startTime = dateFormat.parse("2023-10-01");
        Date endTime = dateFormat.parse("2023-11-01");

        List<OrderDetail> listOrderDetails = orderDetailRepository.findWithProductAndTimeBetween(startTime,endTime);

        Assertions.assertThat(listOrderDetails.size()).isGreaterThan(0);

        for (OrderDetail orderDetail : listOrderDetails){
            System.out.printf("%s | %s | %.2f | %.2f | %.2f \n", orderDetail.getQuantity(), orderDetail.getProduct().getName(), orderDetail.getSubtotal(), orderDetail.getProductCost(), orderDetail.getShippingCost());
        }
    }
}
