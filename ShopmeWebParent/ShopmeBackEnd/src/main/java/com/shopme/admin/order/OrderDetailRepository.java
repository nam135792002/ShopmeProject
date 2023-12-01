package com.shopme.admin.order;

import com.shopme.common.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

    @Query("select new com.shopme.common.entity.OrderDetail(d.product.category.name,d.quantity,d.productCost,d.shippingCost,d.subtotal) from OrderDetail d where d.order.orderTime between ?1 and ?2")
    public List<OrderDetail> findWithCategoryAndTimeBetween(Date startTime, Date endTime);

    @Query("select new com.shopme.common.entity.OrderDetail(d.quantity, d.product.name, d.productCost, d.shippingCost, d.subtotal) from OrderDetail d where d.order.orderTime between ?1 and ?2")
    public List<OrderDetail> findWithProductAndTimeBetween(Date startTime, Date endTime);

}
