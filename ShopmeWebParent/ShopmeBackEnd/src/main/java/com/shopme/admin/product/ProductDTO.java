package com.shopme.admin.product;

public class ProductDTO {
    private String name;
    private String mainImage;
    private float price;
    private float cost;

    public ProductDTO(String name, String mainImage, float price, float cost) {
        this.name = name;
        this.mainImage = mainImage;
        this.price = price;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }
}
