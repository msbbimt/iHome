package com.ihome.dao.impl;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.ihome.dao.AccountDAOImpl_without_spring;
import com.ihome.entity.Account;
import com.ihome.util.HibernateUtil;

/**
 * 用户的CRUD
 * 
 * @author Kinorsi Huang
 * 
 */
@Repository
public class AccountDAOImpl_without_springImpl implements AccountDAOImpl_without_spring
{
	public long addUserAccount(Account userAccount)
	{
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = session.beginTransaction();
		try
		{
			session.save(userAccount);
			tx.commit();
		}
		catch (HibernateException e)
		{
			System.out.println("HibernateException" + e.getMessage());
			tx.rollback();
			return 0;
		}
		return userAccount.getId();
	}

	@Override
	public Account loadAccount(long id)
	{
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = session.beginTransaction();
		Account a = null;
		try
		{
			Object o = session.load(Account.class, id);
			a = (Account)a;
			tx.commit();
		}
		catch (HibernateException e)
		{
			System.out.println("HibernateException" + e.getMessage());
			tx.rollback();
			return null;
		}
		return a;
	}
}
