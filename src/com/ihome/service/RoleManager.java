package com.ihome.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ihome.dao.RoleDAO;
import com.ihome.entity.Role;

@Service
public class RoleManager
{
	@Autowired
	RoleDAO roleDAO;
	
	public List<Role> loadAll()
	{
		return roleDAO.list();
	}
	
	public List<Role> loadRoles(int start, int count)
	{
		return roleDAO.list(start, count);
	}
	
	public long getCount()
	{
		return roleDAO.getCount();
	}
}
