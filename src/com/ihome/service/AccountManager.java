package com.ihome.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ihome.dao.AccountDAO;
import com.ihome.entity.Account;

@Service
public class AccountManager
{
	@Autowired
	AccountDAO accountDAO;
	
	public List<Account> loadAccounts(int start, int count)
	{
		return accountDAO.list(start, count);
	}
	
	public long getCount()
	{
		return accountDAO.getCount();
	}
	
	public int deleteAccount(List<Long> ids)
	{
		return accountDAO.delete(ids);
	}
	
	public void addRolesToAccount(List<Long> roleIds, long accountId)
	{
		for(Long roleId : roleIds)
		{
			accountDAO.addRoleToAccount(roleId, accountId);
		}
	}
	
	public void delRolesToAccount(List<Long> roleIds, long accountId)
	{
		for(Long roleId : roleIds)
		{
			accountDAO.removeRoleFromAccount(roleId, accountId);
		}
	}
}
