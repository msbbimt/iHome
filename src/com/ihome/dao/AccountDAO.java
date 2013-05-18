package com.ihome.dao;

import com.ihome.entity.Account;

public interface AccountDAO extends GenericDAO<Account>
{
//	Account createAccount(Account account);
//	
//	Account loadAccountById(long id);
//	
//	Account loadAccountByName(String name);
	
	void addRoleToAccount(long roleId, long accountId);
	
	void removeRoleFromAccount(long roleId, long accountId);
	
//	void deleteAccount(Account account);
//
//	void updateAccount(Account account);
}