package com.shopme.admin.brand;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.shopme.admin.paging.PagingAndSortingHeper;
import com.shopme.common.entity.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class BrandService {
    public static final int BRANDS_PER_PAGE = 4;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private Cloudinary cloudinary;

    public List<Brand> listAllTemp(){
        return (List<Brand>) brandRepository.findAllTemp();
    }

    public List<Brand> listAll(){
        return brandRepository.findAll(Sort.by("name").ascending());
    }

    public void listByPage(int pageNum, PagingAndSortingHeper helper){
        helper.listEntities(pageNum, BRANDS_PER_PAGE,brandRepository);
    }

    public Brand save(Brand brand){
        return brandRepository.save(brand);
    }

    public Brand get(Integer id) throws BrandNotFoundException{
        try{
            return brandRepository.findById(id).get();
        }catch (NoSuchElementException ex){
            throw new BrandNotFoundException("Could not find any brand with ID " + id);
        }
    }

    public void delete(Integer id) throws BrandNotFoundException{
        Long countById = brandRepository.countById(id);
        if(countById == null || countById == 0){
            throw new BrandNotFoundException("Could not find any brand with ID " + id);
        }
        brandRepository.deleteById(id);
    }

    public void deleteImageInCloudinary(Brand brand) throws IOException {
        String urlTemp = brand.getLogo();
        int lastSlashIndex = urlTemp.lastIndexOf('/');
        int lastDotIndex = urlTemp.lastIndexOf('.');
        String fileName = urlTemp.substring(lastSlashIndex + 1, lastDotIndex);
        System.out.println(fileName);
        cloudinary.uploader().destroy(fileName, ObjectUtils.asMap("resource_type","image"));
    }

    public String checkUnique(Integer id, String name){
        boolean isCreatingNew = (id == null || id == 0);
        Brand brandByName = brandRepository.findByName(name);
        if(isCreatingNew){
            if(brandByName != null) return "Duplicate";
        }else{
            if(brandByName != null && !Objects.equals(brandByName.getId(), id)){
                return "Duplicate";
            }
        }
        return "OK";
    }
}
