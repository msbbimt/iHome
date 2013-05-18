package com.ihome.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ihome.dao.PermissionDAO;
import com.ihome.entity.Permission;

@Service
public class PermissonManager
{
	@Autowired
	PermissionDAO perDAO;
	
	public void addSubPermisson(long parent, long child)
	{
		
	}
	
	public List<Permission> loadPermissionTree()
	{
		List<Permission> tree = perDAO.loadPermissionTree();
		return tree;
	}
}
