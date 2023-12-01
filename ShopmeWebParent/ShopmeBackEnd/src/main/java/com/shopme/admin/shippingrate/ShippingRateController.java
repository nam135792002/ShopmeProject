package com.shopme.admin.shippingrate;

import com.shopme.admin.paging.PagingAndSortingHeper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ShippingRateController {
    private String defaultRedirectURL = "redirect:/shipping_rates/page/1?sortField=country&sortDir=asc";
    @Autowired private ShippingRateService service;

    @GetMapping("/shipping_rates")
    public String listFirstPage(){
        return defaultRedirectURL;
    }

    @GetMapping("/shipping_rates/page/{pageNum}")
    public String listByPage(@PagingAndSortingParam(listName = "shippingRates")PagingAndSortingHeper heper,
                             @PathVariable(name = "pageNum") int pageNum){
        service.listByPage(pageNum,heper);
        return "shipping_rates/shipping_rates";
    }

    @GetMapping("/shipping_rates/new")
    public String newRate(Model model){
        List<Country> listCountries = service.listAllCountries();

        model.addAttribute("rate",new ShippingRates());
        model.addAttribute("listCountries",listCountries);
        model.addAttribute("pageTitle","New Rate");

        return "shipping_rates/shipping_rate_form";
    }

    @PostMapping("/shipping_rates/save")
    public String saveRate(ShippingRates rates, RedirectAttributes rs){
        try {
            service.save(rates);
            rs.addFlashAttribute("message","The shipping rate has been saved successfully.");
        } catch (ShippingRateAlreadyExistsException e) {
            rs.addFlashAttribute("message",e.getMessage());
        }
        return defaultRedirectURL;
    }

    @GetMapping("/shipping_rates/edit/{id}")
    public String editRate(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes rs){
        try {
            ShippingRates rates = service.get(id);
            List<Country> listCountries = service.listAllCountries();

            model.addAttribute("listCountries",listCountries);
            model.addAttribute("rate",rates);
            model.addAttribute("pageTitle", "Edit rate(ID: " + id + ")");

            return "shipping_rates/shipping_rate_form";
        } catch (ShippingrateNotFoundException e) {
            rs.addFlashAttribute("message",e.getMessage());
            return defaultRedirectURL;
        }
    }

    @GetMapping("/shipping_rates/cod/{id}/enabled/{supported}")
    public String updateCODSupport(@PathVariable(name = "id") Integer id,
                                    @PathVariable(name = "supported") boolean supported,
                                   Model model, RedirectAttributes rs){
        try {
            service.updateCODSupport(id,supported);
            rs.addFlashAttribute("message","COD support for shipping rate ID " + id + " has been updated successfully");
        } catch (ShippingrateNotFoundException e) {
            rs.addFlashAttribute("message",e.getMessage());
        }
        return defaultRedirectURL;
    }

    @GetMapping("/shipping_rates/delete/{id}")
    public String deleteRate(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes rs){
        try {
            service.delete(id);
            rs.addFlashAttribute("message","The shipping rate ID " + id + " has been deleted.");
        } catch (ShippingrateNotFoundException e) {
            rs.addFlashAttribute("message",e.getMessage());
        }
        return defaultRedirectURL;
    }
}
