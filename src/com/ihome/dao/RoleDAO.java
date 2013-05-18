package com.ihome.dao;

import com.ihome.entity.Role;

public interface RoleDAO extends GenericDAO<Role>
{
	Role loadByRoleName(String roleName);
	Role createByRoleName(String roleName);
}
