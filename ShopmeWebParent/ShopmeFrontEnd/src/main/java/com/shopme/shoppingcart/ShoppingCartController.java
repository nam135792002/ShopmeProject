package com.shopme.shoppingcart;

import com.shopme.Utility;
import com.shopme.address.AddressService;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.ShippingRates;
import com.shopme.customer.CustomerNotFoundException;
import com.shopme.customer.CustomerService;
import com.shopme.shipping.ShippingRateService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ShoppingCartController {
    @Autowired private CartItemService cartItemService;
    @Autowired private CustomerService customerService;
    @Autowired private AddressService addressService;
    @Autowired private ShippingRateService shippingRateService;

    @GetMapping("/cart")
    public String viewCart(Model model, HttpServletRequest request) throws CustomerNotFoundException {
        Customer customer = getAuthenticatsCustomer(request);
        List<CartItem> cartItems = cartItemService.listCartItem(customer);
        float estimatedTotal = 0.0F;
        for (CartItem item : cartItems){
            estimatedTotal += item.getSubTotal();
        }
        Address defaultAddress = addressService.getDefaultAddress(customer);
        ShippingRates shippingRates = null;
        boolean usePrimaryAddressDefault = false;
        if(defaultAddress != null){
            shippingRates = shippingRateService.getShippingRateForAddress(defaultAddress);
        }else{
            usePrimaryAddressDefault = true;
            shippingRates = shippingRateService.getShippingRateForCustomer(customer);
        }

        model.addAttribute("usePrimaryAddressDefault",usePrimaryAddressDefault);
        model.addAttribute("shippingSupported",shippingRates != null);
        model.addAttribute("cartItems",cartItems);
        model.addAttribute("estimatedTotal",estimatedTotal);
        return "cart/shopping_cart";
    }

    private Customer getAuthenticatsCustomer(HttpServletRequest request) throws CustomerNotFoundException {
        String email = Utility.getEmailOfAuthenticatedCustomer(request);
        return customerService.getCustomerByEmail(email);
    }
}
