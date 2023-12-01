package com.shopme.admin.order;

import com.shopme.admin.paging.PagingAndSortingHeper;
import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderService {
    private static final int ORDER_PER_PAGE = 4;

    @Autowired private OrderRepository repository;
    @Autowired private CountryRepository countryRepository;

    public void listByPage(int pageNum, PagingAndSortingHeper heper){
        String sortField = heper.getSortField();
        String sortDir = heper.getSortDir();
        String keyword = heper.getKeyword();

        Sort sort = null;

        if("destination".equals(sortField)){
            sort = Sort.by("country").and(Sort.by("state")).and(Sort.by("city"));
        }else {
            sort = Sort.by(sortField);
        }
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum - 1, ORDER_PER_PAGE, sort);

        Page<Order> page = null;

        if(keyword != null){
            page = repository.findAll(keyword, pageable);
        }else{
            page = repository.findAll(pageable);
        }
        heper.updateModelAttributes(pageNum, page);
    }

    public Order get(Integer id) throws OrderNotFoundException {
        try {
            return repository.findById(id).get();
        } catch (NoSuchElementException ex){
            throw new OrderNotFoundException("Could not find any orders with ID " + id);
        }
    }

    public void delete(Integer id) throws OrderNotFoundException {
        Long count = repository.countById(id);
        if(count == null || count == 0){
            throw new OrderNotFoundException("Could not find any orders with ID " + id);
        }
        repository.deleteById(id);
    }

    public List<Country> listAllCountries(){
        return countryRepository.findAllByOrderByNameAsc();
    }

    public void save(Order order) {
    }
}
