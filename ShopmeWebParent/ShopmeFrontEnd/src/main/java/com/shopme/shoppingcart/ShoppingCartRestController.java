package com.shopme.shoppingcart;

import com.shopme.Utility;
import com.shopme.common.entity.Customer;
import com.shopme.customer.CustomerNotFoundException;
import com.shopme.customer.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShoppingCartRestController {
    @Autowired private CartItemService cartItemService;
    @Autowired private CustomerService customerService;

    @PostMapping("/cart/add/{productId}/{quantity}")
    public String addProductToCart(@PathVariable(name = "productId") Integer productId,
                                   @PathVariable("quantity") int quantity,
                                   HttpServletRequest request){
        try {
            Customer customer = getAuthenticatsCustomer(request);
            Integer updatedQuantity = cartItemService.addProduct(productId, quantity, customer);

            return updatedQuantity + "item(s) of this product were added to your shopping cart.";
        } catch (CustomerNotFoundException e) {
            return "You must login to add this product to cart.";
        } catch (ShoppingCartException e) {
            return e.getMessage();
        }
    }

    private Customer getAuthenticatsCustomer(HttpServletRequest request) throws CustomerNotFoundException {
        String email = Utility.getEmailOfAuthenticatedCustomer(request);
        if(email == null){
            throw new CustomerNotFoundException("No authenticated customer");
        }
        return customerService.getCustomerByEmail(email);
    }

    @PostMapping("/cart/update/{productId}/{quantity}")
    public String updateQuantity(@PathVariable(name = "productId") Integer productId,
                                   @PathVariable("quantity") int quantity,
                                   HttpServletRequest request) {
        try {
            Customer customer = getAuthenticatsCustomer(request);
            float subTotal = cartItemService.updatedQuantity(productId, quantity, customer);

            return String.valueOf(subTotal);
        } catch (CustomerNotFoundException e) {
            return "You must login to change quantity to cart.";
        }
    }

    @DeleteMapping("/cart/remove/{productId}")
    public String removeProduct(@PathVariable("productId") Integer productId, HttpServletRequest request){
        try {
            Customer customer = getAuthenticatsCustomer(request);
            cartItemService.removeProduct(productId, customer);
            return "The product has been removed form your shopping cart.";
        } catch (CustomerNotFoundException e) {
            return "You must login to remove product.";
        }

    }
}
