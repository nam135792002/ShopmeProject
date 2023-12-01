package com.shopme.admin.product;

import com.shopme.common.entity.Product;
import com.shopme.common.entity.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    public Product findByName(String name);

    public Long countById(Integer id);

    @Query("select p from ProductImage p where p.product.id = ?1")
    public List<ProductImage> findExtraImageByProduct(Integer id);

    @Query("select count(*) from ProductImage p where p.product.id = ?1")
    public Long countImageExtraById(Integer id);

    @Query("select p from Product  p where p.name like %?1% " +
            "or p.shortDescription like %?1% " +
            "or p.fullDescription like %?1% " +
            "or p.brand.name like %?1% " +
            "or p.category.name like %?1%")
    public Page<Product> findAll(String keyword, Pageable pageable);

    @Query("select p from Product p where p.category.id = ?1 " +
            "or p.category.allParentsIDs like %?2%")
    public Page<Product> findAllInCategory(Integer categoryId, String categoryInMatch, Pageable pageable);

    @Query("select p from Product p where (p.category.id = ?1 " +
            "or p.category.allParentsIDs like %?2%) and " +
            "(p.name like %?3% " +
            "or p.shortDescription like %?3% " +
            "or p.fullDescription like %?3% " +
            "or p.brand.name like %?3% " +
            "or p.category.name like %?3%)")
    public Page<Product> searchInCategory(Integer categoryId, String categoryInMatch, String keyword ,Pageable pageable);

    @Query("update Product  p set p.enabled = ?2 where p.id = ?1")
    @Modifying
    public void updateEnabledStatus(Integer id, boolean enabled);

    @Query("select p from Product p where p.name like %?1%")
    public Page<Product> searchProductByName(String keyword, Pageable pageable);
}
