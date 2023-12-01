package com.shopme.admin.product;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.shopme.admin.paging.PagingAndSortingHeper;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.ProductImage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@Transactional
public class ProductService {

    public static final int PRODUCTS_PER_PAGE = 5;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private Cloudinary cloudinary;
    public List<Product> listAll(){
        return productRepository.findAll();
    }

    public Page<Product> listByPage(int pageNum, String sortField, String sortDir, String keyword, Integer categoryId){
        Sort sort = Sort.by(sortField);

        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum-1, PRODUCTS_PER_PAGE, sort);
        if(keyword != null && !keyword.isEmpty()){
            if(categoryId != null && categoryId > 0){
                String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
                return productRepository.searchInCategory(categoryId, categoryIdMatch, keyword, pageable);
            }
            return productRepository.findAll(keyword,pageable);
        }
        if(categoryId != null && categoryId > 0){
            String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
            return productRepository.findAllInCategory(categoryId, categoryIdMatch, pageable);
        }
        return productRepository.findAll(pageable);
    }

    public Product save(Product product){
        if(product.getId() == null){
            product.setCreatedTime(new Date());
        }
        if(product.getAlias() == null || product.getAlias().isEmpty()){
            String defaultAlias = product.getName().replaceAll(" ","-");
            product.setAlias(defaultAlias);
        }else{
            product.setAlias(product.getAlias().replaceAll(" ","-"));
        }
        product.setUpdatedTime(new Date());
        return productRepository.save(product);
    }

    public void saveProductPrice(Product product){
        Product productInDB = productRepository.findById(product.getId()).get();
        productInDB.setCost(product.getCost());
        productInDB.setPrice(product.getPrice());
        productInDB.setDiscountPercent(product.getDiscountPercent());
        productRepository.save(productInDB);
    }

    public String checkUnique(Integer id, String name){
        boolean isCreatingNew = (id == null || id == 0);
        Product productByName = productRepository.findByName(name);
        if(isCreatingNew){
            if(productByName != null) return "Duplicate";
        }else{
            if(productByName != null && !Objects.equals(productByName.getId(), id)){
                return "Duplicate";
            }
        }
        return "OK";
    }

    public void updateProductEnabledStatus(Integer id, boolean enabled){
        productRepository.updateEnabledStatus(id,enabled);
    }

    public void delete(Integer id) throws ProductNotFoundException {
        Long countById = productRepository.countById(id);
        if(countById == null || countById == 0){
            throw new ProductNotFoundException("Could not find any product with ID " + id);
        }
        productRepository.deleteById(id);
    }

    public Product get(Integer id) throws ProductNotFoundException {
        try{
            return productRepository.findById(id).get();
        }catch (NoSuchElementException ex){
            throw new ProductNotFoundException("Could not find any category with ID " + id);
        }
    }

    public void deleteImageInCloudinary(Product product) throws IOException {
        String urlTemp = product.getMainImage();
        int lastSlashIndex = urlTemp.lastIndexOf('/');
        int lastDotIndex = urlTemp.lastIndexOf('.');
        String fileName = urlTemp.substring(lastSlashIndex + 1, lastDotIndex);
        cloudinary.uploader().destroy(fileName, ObjectUtils.asMap("resource_type","image"));
    }

    public void deleteImageExtraInCloudinary(Integer id) throws IOException {
        List<ProductImage> listProductImages = productRepository.findExtraImageByProduct(id);
        for(ProductImage productImage : listProductImages){
            String urlTemp = productImage.getUrl();
            int lastSlashIndex = urlTemp.lastIndexOf('/');
            int lastDotIndex = urlTemp.lastIndexOf('.');
            String fileName = urlTemp.substring(lastSlashIndex + 1, lastDotIndex);
            cloudinary.uploader().destroy(fileName, ObjectUtils.asMap("resource_type","image"));
        }
    }

    public List<ProductImage> findAllExtraImage(Integer id){
        return productRepository.findExtraImageByProduct(id);
    }

    public long countByImageExtra(Integer id){
        return productRepository.countImageExtraById(id);
    }

    public void searchProduct(int pageNum, PagingAndSortingHeper heper){
        Pageable pageable = heper.createPageable(PRODUCTS_PER_PAGE, pageNum);
        String keyword = heper.getKeyword();

        Page<Product> page = productRepository.searchProductByName(keyword,pageable);

        heper.updateModelAttributes(pageNum,page);
    }
}
