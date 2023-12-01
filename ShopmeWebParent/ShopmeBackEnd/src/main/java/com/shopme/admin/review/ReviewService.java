package com.shopme.admin.review;

import com.shopme.admin.paging.PagingAndSortingHeper;
import com.shopme.common.entity.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class ReviewService {
    public static final int REVIEWS_PER_PAGE = 5;

    @Autowired private ReviewRepository repository;

    public void listByPage(int pageNum, PagingAndSortingHeper heper){
        heper.listEntities(pageNum, REVIEWS_PER_PAGE,repository);
    }

    public Review get(Integer id) throws ReviewNotFoundException {
        try {
            return repository.findById(id).get();
        }catch (NoSuchElementException ex){
            throw new ReviewNotFoundException("Could not find any reviews with ID "+ id);
        }
    }

    public void save(Review reviewInForm){
        Review reviewInDB = repository.findById(reviewInForm.getId()).get();
        reviewInDB.setHeadline(reviewInForm.getHeadline());
        reviewInDB.setComment(reviewInForm.getComment());

        repository.save(reviewInDB);
    }

    public void delete(Integer id) throws ReviewNotFoundException {
        if (!repository.existsById(id)){
            throw new ReviewNotFoundException("Could not find any reviews with ID " + id);
        }

        repository.deleteById(id);
    }
}
