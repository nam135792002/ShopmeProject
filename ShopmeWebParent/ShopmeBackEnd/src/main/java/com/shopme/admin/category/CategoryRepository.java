package com.shopme.admin.category;

import com.shopme.common.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query("select c from Category c where c.parent.id is null")
    public List<Category> listRootCategories(Sort sort);

    @Query("select c from Category c where c.parent.id is null")
    public Page<Category> listRootCategories(Pageable pageable);

    @Query("select c from Category c where c.name like %?1%")
    public Page<Category> search(String keyword, Pageable pageable);
    public Long countById(Integer id);

    public Category findByName(String name);

    public Category findByAlias(String alias);

    @Query("update Category  c set c.enabled = ?2 where c.id = ?1")
    @Modifying
    public void updateEnabledStatus(Integer id, boolean enabled);
}

