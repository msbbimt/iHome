package com.ihome.dao;

import java.util.List;

import com.ihome.entity.Permission;

public interface PermissionDAO extends GenericDAO<Permission>
{
	void addPermissonToRole(long permissionId, long roleId);
	
	void removePermissionFromRole(long permissionId, long roleId);
	
	void addSubToParent(long parentId, long subId);
	
	List<Permission> loadPermissionTree();
}
