package com.shopme.common.mapper;

import com.shopme.common.dto.RoleDTO;
import com.shopme.common.entity.Role;

public class RoleMapper {
    public static RoleDTO mapToRoleDTO(Role role){
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());
        roleDTO.setDescription(role.getDescription());
        return roleDTO;
    }

    public static Role mapToRole(RoleDTO roleDTO){
        Role role = new Role();
        role.setId(roleDTO.getId());
        role.setName(role.getName());
        role.setDescription(role.getDescription());
        return role;
    }
}
