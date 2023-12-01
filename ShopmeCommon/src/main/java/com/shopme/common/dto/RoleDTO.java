package com.shopme.common.dto;

import java.util.Objects;

public class RoleDTO {
    private int id;
    private String name;
    private String description;

    public RoleDTO() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RoleDTO other = (RoleDTO) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
