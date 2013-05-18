package com.ihome.dao;

import com.ihome.entity.Account;

public interface AccountDAOImpl_without_spring
{
	public long addUserAccount(Account userAccount);
	
	public Account loadAccount(long id);
}
