package com.shopme.checkout;

import com.shopme.Utility;
import com.shopme.address.AddressService;
import com.shopme.common.entity.*;
import com.shopme.config.VnPayConfig;
import com.shopme.customer.CustomerNotFoundException;
import com.shopme.customer.CustomerService;
import com.shopme.order.OrderService;
import com.shopme.setting.EmailSettingBag;
import com.shopme.setting.PaymentSettingBag;
import com.shopme.setting.SettingService;
import com.shopme.shipping.ShippingRateService;
import com.shopme.shoppingcart.CartItemService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class CheckoutController {
    @Autowired private CheckoutService checkoutService;
    @Autowired private CustomerService customerService;
    @Autowired private AddressService addressService;
    @Autowired private ShippingRateService shippingRateService;
    @Autowired private CartItemService cartItemService;
    @Autowired private OrderService orderService;
    @Autowired private SettingService settingService;

    @GetMapping("/checkout")
    public String chowCheckoutPage(Model model, HttpServletRequest request) throws CustomerNotFoundException {
        Customer customer = getAuthenticatsCustomer(request);

        Address defaultAddress = addressService.getDefaultAddress(customer);
        ShippingRates shippingRates = null;
        if(defaultAddress != null){
            model.addAttribute("shippingAddress",defaultAddress.toString());
            shippingRates = shippingRateService.getShippingRateForAddress(defaultAddress);
        }else{
            model.addAttribute("shippingAddress",customer.toString());
            shippingRates = shippingRateService.getShippingRateForCustomer(customer);
        }

        if(shippingRates == null){
            return "redirect:/cart";
        }

        List<CartItem> cartItems = cartItemService.listCartItem(customer);
        CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems, shippingRates);
        String currencyCode = settingService.getCurrencyCode();
        PaymentSettingBag paymentSettingBag = settingService.getPaymentSettings();
        String clientId = paymentSettingBag.getClientID();

        model.addAttribute("checkoutInfo", checkoutInfo);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("currencyCode",currencyCode);
        model.addAttribute("customer",customer);
        model.addAttribute("clientId",clientId);

        return "checkout/checkout";
    }

    private Customer getAuthenticatsCustomer(HttpServletRequest request) throws CustomerNotFoundException {
        String email = Utility.getEmailOfAuthenticatedCustomer(request);
        return customerService.getCustomerByEmail(email);
    }

   @PostMapping("/place_order")
    public String placeOrder(HttpServletRequest request) throws CustomerNotFoundException, MessagingException, UnsupportedEncodingException {
        String paymentType = request.getParameter("paymentMethod");
        PaymentMethod paymentMethod = PaymentMethod.valueOf(paymentType);

       Customer customer = getAuthenticatsCustomer(request);

       Address defaultAddress = addressService.getDefaultAddress(customer);
       ShippingRates shippingRates = null;
       if(defaultAddress != null){
           shippingRates = shippingRateService.getShippingRateForAddress(defaultAddress);
       }else{
           shippingRates = shippingRateService.getShippingRateForCustomer(customer);
       }

       List<CartItem> cartItems = cartItemService.listCartItem(customer);
       CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems, shippingRates);

        Order createdOrder = orderService.createOrder(customer, defaultAddress, cartItems, paymentMethod, checkoutInfo);
        cartItemService.deleteByCustomer(customer);
        sendOrderConfirmationEmail(request, createdOrder);
       return "checkout/order_completed";
    }

    private void sendOrderConfirmationEmail(HttpServletRequest request, Order order) throws MessagingException, UnsupportedEncodingException {
        EmailSettingBag emailSettingBags = settingService.getEmailSettings();
        JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettingBags);
        mailSender.setDefaultEncoding("utf-8");

        String toAddress = order.getCustomer().getEmail();
        String subject = emailSettingBags.getOrderConfirmationSubject();
        String content = emailSettingBags.getOrderConfirmationContent();

        subject = subject.replace("[[orderId]]", String.valueOf(order.getId()));

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(emailSettingBags.getForm(), emailSettingBags.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);

        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss E, dd MMM yyyy");
        String orderTime = dateFormat.format(order.getOrderTime());

        content = content.replace("[[name]]",order.getCustomer().getFullName());
        content = content.replace("[[orderId]]", String.valueOf(order.getId()));
        content = content.replace("[[orderTime]]",orderTime);
        content = content.replace("[[shippingAddress]]",order.getShippingAddress());
        content = content.replace("[[paymentMethod]]", order.getPaymentMethod().toString());
        content = content.replace("[[total]]", String.valueOf(order.getTotal()));

        helper.setText(content,true);
        mailSender.send(message);
    }

    @PostMapping ("/api/payment/create_payment")
    public String createPayment(HttpServletRequest request, @RequestParam("total") String total) throws UnsupportedEncodingException {
        String orderType = "other";
        double subTotal = Double.parseDouble(total);
        long amount = (long) (subTotal * 100 * 1000);
        String vnp_TxnRef = VnPayConfig.getRandomNumber(8);
        String vnp_IpAddr = VnPayConfig.getIpAddress(request);

        String vnp_TmnCode = VnPayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VnPayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VnPayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
//        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", VnPayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VnPayConfig.hmacSHA512(VnPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VnPayConfig.vnp_PayUrl + "?" + queryUrl;

        return "redirect:" + paymentUrl;
    }

    @GetMapping("/payment_info")
    public String paymentSuccess(HttpServletRequest request, @RequestParam(value = "vnp_ResponseCode") String status) throws CustomerNotFoundException, MessagingException, UnsupportedEncodingException {
        if(status.equals("00")){
            PaymentMethod paymentMethod = PaymentMethod.valueOf("VNPay");

            Customer customer = getAuthenticatsCustomer(request);

            Address defaultAddress = addressService.getDefaultAddress(customer);
            ShippingRates shippingRates = null;
            if(defaultAddress != null){
                shippingRates = shippingRateService.getShippingRateForAddress(defaultAddress);
            }else{
                shippingRates = shippingRateService.getShippingRateForCustomer(customer);
            }

            List<CartItem> cartItems = cartItemService.listCartItem(customer);
            CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems, shippingRates);

            Order createdOrder = orderService.createOrder(customer, defaultAddress, cartItems, paymentMethod, checkoutInfo);
            cartItemService.deleteByCustomer(customer);
            sendOrderConfirmationEmail(request, createdOrder);
            return "redirect:/payment_info/success";
        }else{
            return "redirect:/payment_info/fail";
        }
    }

    @GetMapping("/payment_info/success")
    public String paymentSuccess(){
        return "checkout/order_completed";
    }

    @GetMapping("/payment_info/fail")
    public String paymentFail(){
        return "checkout/order_fail";
    }
}
