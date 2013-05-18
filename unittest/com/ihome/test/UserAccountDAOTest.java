package com.ihome.test;

import junit.framework.Assert;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Test;

import com.ihome.entity.Account;
import com.ihome.util.HibernateUtil;

public class UserAccountDAOTest
{
	@Test
	public void testAddUserAccount()
	{
		Account userAccount = new Account();
		userAccount.setPassword("testpassword");
		userAccount.setUsername("testuser");

		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.save(userAccount);
		tx.commit();

		Assert.assertTrue(userAccount.getId() > 0);
	}


}
