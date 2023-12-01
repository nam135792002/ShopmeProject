package com.shopme.admin.review;

import com.shopme.admin.paging.SearchRepository;
import com.shopme.common.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends SearchRepository<Review, Integer> {

    @Query("select r from Review r where r.headline like %?1% or r.comment like %?1% or r.product.name like %?1% or concat(r.customer.firstName,' ',r.customer.lastName) like %?1%")
    public Page<Review> findAll(String keyword, Pageable pageable);

    public List<Review> findAll();
}
