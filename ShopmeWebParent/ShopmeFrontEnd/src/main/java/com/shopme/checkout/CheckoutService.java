package com.shopme.checkout;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.ShippingRates;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckoutService {
    private static final int DIM_DIVISOR = 139;
    public CheckoutInfo prepareCheckout(List<CartItem> cartItems, ShippingRates shippingRates){
        CheckoutInfo checkoutInfo = new CheckoutInfo();

        float productCost = calculateProductCost(cartItems);
        float productTotal = calculateProductTotal(cartItems);
        float shippingCost = calculateShippingCost(cartItems, shippingRates);
        float paymentTotal = productTotal + shippingCost;

        checkoutInfo.setProductCost(productCost);
        checkoutInfo.setProductTotal(productTotal);
        checkoutInfo.setDeliverDays(shippingRates.getDays());
        checkoutInfo.setCodSupported(shippingRates.isCodSupported());
        checkoutInfo.setShippingCostTotal(shippingCost);
        checkoutInfo.setPaymentTotal(paymentTotal);

        return checkoutInfo;
    }

    private float calculateShippingCost(List<CartItem> cartItems, ShippingRates shippingRates) {
        float shippingCosTotal = 0.0f;
        for(CartItem item : cartItems){
            Product product = item.getProduct();
            float dimWeight = (product.getLength() * product.getWidth() * product.getHeight()) / DIM_DIVISOR;
            float finalWeight = product.getWeight() > dimWeight ? product.getWeight() : dimWeight;
            float shippingCost = finalWeight * item.getQuantity() * shippingRates.getRate();
            item.setShippingCost(shippingCost);
            shippingCosTotal += shippingCost;
        }
        return shippingCosTotal;
    }

    private float calculateProductTotal(List<CartItem> cartItems) {
        float total = 0.0f;

        for(CartItem item : cartItems){
            total += item.getSubTotal();
        }
        return total;
    }

    private float calculateProductCost(List<CartItem> cartItems) {
        float cost = 0.0f;

        for(CartItem item : cartItems){
            cost += item.getQuantity() * item.getProduct().getCost();
        }
        return cost;
    }
}
