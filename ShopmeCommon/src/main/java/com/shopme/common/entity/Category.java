package com.shopme.common.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 128, nullable = false, unique = true)
    private String name;

    @Column(length = 64, nullable = false, unique = true)
    private String alias;

    @Column(length = 128, nullable = false)
    private String image;

    @Column(name = "all_parents_ids", length = 256, nullable = true)
    private String allParentsIDs;

    private boolean enabled;

    @OneToOne
    @JoinColumn(name = "parent_id",unique = false)
    private Category parent;

    @OneToMany(mappedBy = "parent")
    @OrderBy("name asc")
    private Set<Category> children = new HashSet<>();

    public Category() {

    }

    public Category(Integer id) {
        this.id = id;
    }

    public Category(String name) {
        this.name = name;
        this.alias = name;
        this.image = "default.png";
    }

    public Category(Integer id, String name, String alias) {
        this.id = id;
        this.name = name;
        this.alias = alias;
    }

    public static Category copyIdAndName(Category category) {
        Category category1 = new Category();
        category1.setId(category.getId());
        category1.setName(category.getName());
        return category1;
    }

    public static Category copyIdAndName(Integer id, String name) {
        Category category1 = new Category();
        category1.setId(id);
        category1.setName(name);
        return category1;
    }

    public static Category copyFull(Category category){
        Category category1 = new Category();
        category1.setId(category.getId());
        category1.setName(category.getName());
        category1.setImage(category.getImage());
        category1.setAlias(category.getAlias());
        category1.setEnabled(category.isEnabled());
        category1.setHasChildren(category.getChildren().size() > 0);

        return category1;
    }

    public static Category copyFull(Category category, String name){
        Category copyCategory = Category.copyFull(category);
        copyCategory.setName(name);
        return copyCategory;
    }

    public Category(String name, Category category) {
        this(name);
        this.parent = category;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public Set<Category> getChildren() {
        return children;
    }

    public void setChildren(Set<Category> children) {
        this.children = children;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public String getAllParentsIDs() {
        return allParentsIDs;
    }

    public void setAllParentsIDs(String allParentsIDs) {
        this.allParentsIDs = allParentsIDs;
    }

    @Transient
    private boolean hasChildren;

    @Override
    public String toString() {
        return name;
    }
}
