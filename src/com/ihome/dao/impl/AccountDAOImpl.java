package com.ihome.dao.impl;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.ihome.dao.AccountDAO;
import com.ihome.entity.Account;
import com.ihome.entity.Role;

/**
 * 用户的CRUD
 * 
 * @author Kinorsi Huang
 * 
 */
@Repository
public class AccountDAOImpl extends GenericHibernateDAO<Account> implements AccountDAO 
{
	@Override
	public void removeRoleFromAccount(long roleId, long accountId)
	{
		Session session = sessionFactory.getCurrentSession();
		Account account = (Account) session.load(Account.class, accountId);
		Role role = (Role)session.load(Role.class, roleId);
		account.removeFromRole(role);
	}
	
	@Override
	public void addRoleToAccount(long roleId, long accountId)
	{
		Session session = sessionFactory.getCurrentSession();
		Account account = (Account) session.load(Account.class, accountId);
		Role role = (Role)session.load(Role.class, roleId);
		account.addToRole(role);
	}
}
