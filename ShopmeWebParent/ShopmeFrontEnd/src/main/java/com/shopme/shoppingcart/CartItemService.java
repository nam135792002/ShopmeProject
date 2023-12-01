package com.shopme.shoppingcart;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Product;
import com.shopme.product.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CartItemService {
    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private ProductRepository productRepository;
    public Integer addProduct(Integer productId, Integer quantity, Customer customer) throws ShoppingCartException {
        Integer updatedQuantity = quantity;
        Product product = new Product(productId);

        CartItem cartItem = cartItemRepository.findByCustomerAndProduct(customer,product);

        if(cartItem != null){
            updatedQuantity = cartItem.getQuantity() + quantity;
            if(updatedQuantity > 5){
                throw new ShoppingCartException("Could not add " + quantity + " item(s) " +
                        "because there's a already " + cartItem.getQuantity() + " item(s) " +
                        "in your shopping cart. Maximum allowed quantity is 5.");
            }
        }else{
            cartItem = new CartItem();
            cartItem.setCustomer(customer);
            cartItem.setProduct(product);
        }
        cartItem.setQuantity(updatedQuantity);
        cartItemRepository.save(cartItem);
        return updatedQuantity;
    }

    public List<CartItem> listCartItem(Customer customer){
        return cartItemRepository.findByCustomer(customer);
    }

    public float updatedQuantity(Integer productId, Integer quantity, Customer customer){
        cartItemRepository.updateQuantity(quantity,customer.getId(), productId);
        Product product = productRepository.findById(productId).get();
        return product.getDiscountPrice() * quantity;
    }

    public void removeProduct(Integer productId, Customer customer){
        cartItemRepository.deleteByCustomerAndProduct(customer.getId(), productId);
    }

    public void deleteByCustomer(Customer customer){
        cartItemRepository.deleteByCustomer(customer.getId());
    }
}
