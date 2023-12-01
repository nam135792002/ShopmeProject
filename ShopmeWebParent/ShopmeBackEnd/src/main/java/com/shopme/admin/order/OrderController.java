package com.shopme.admin.order;

import com.shopme.admin.paging.PagingAndSortingHeper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.setting.SettingService;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Order;
import com.shopme.common.entity.Setting;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class OrderController {
    private String defaultReditrectURL = "redirect:/orders/page/1?sortField=orderTime&sortDir=desc";

    @Autowired private OrderService orderService;
    @Autowired private SettingService settingService;

    @GetMapping("/orders")
    public String listFirstPage(){
        return defaultReditrectURL;
    }

    @GetMapping("/orders/page/{pageNum}")
    public String listByPage(@PagingAndSortingParam(listName = "listOrders")PagingAndSortingHeper heper,
                             @PathVariable(name = "pageNum") int pageNum, HttpServletRequest request){
        orderService.listByPage(pageNum,heper);
        loadCurrencySetting(request);
        return "orders/orders";
    }

    private void loadCurrencySetting(HttpServletRequest request) {
        List<Setting> currencySettings = settingService.getCurrencySettings();

        for (Setting setting : currencySettings){
            request.setAttribute(setting.getKey(),setting.getValue());
        }
    }

    @GetMapping("/orders/detail/{id}")
    public String viewOrderDetails(@PathVariable("id") Integer id, Model model,
                                   RedirectAttributes rs, HttpServletRequest request){
        try {
            Order order = orderService.get(id);
            loadCurrencySetting(request);
            model.addAttribute("order",order);

            return "orders/order_detail_modal";
        } catch (OrderNotFoundException e) {
            rs.addFlashAttribute("message",e.getMessage());
            return defaultReditrectURL;
        }
    }

    @GetMapping("/orders/delete/{id}")
    public String deleteOrder(@PathVariable("id") Integer id, Model model, RedirectAttributes rs){
        try {
            orderService.delete(id);
            rs.addFlashAttribute("message","The order ID " + id + " has been deleted.");
        }catch (OrderNotFoundException ex){
            rs.addFlashAttribute("message",ex.getMessage());
        }
        return defaultReditrectURL;
    }

    @GetMapping("/orders/edit/{id}")
    public String editOrder(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request){
        try{
            Order order = orderService.get(id);

            List<Country> listCountries = orderService.listAllCountries();

            model.addAttribute("pageTitle","Edit Order (ID: " + id + ")");
            model.addAttribute("order",order);
            model.addAttribute("listCountries",listCountries);

            return "orders/order_form";
        }catch (OrderNotFoundException ex){
            redirectAttributes.addFlashAttribute("message",ex.getMessage());
            return defaultReditrectURL;
        }
    }

    @PostMapping("/orders/save")
    public String saveOrder(Order order, HttpServletRequest request, RedirectAttributes redirectAttributes){
        System.out.println("Country: " + order.getCountry());
        System.out.println("Total: " + order.getTotal());
        orderService.save(order);
        redirectAttributes.addFlashAttribute("message","The order ID " + order.getId() + " has been updated successfully.");
        return defaultReditrectURL;
    }
}
