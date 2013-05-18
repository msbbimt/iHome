package com.ihome.test;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ihome.entity.Account;
import com.ihome.entity.Role;
import com.ihome.service.usermanage.UserManager;
import com.ihome.util.HibernateUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:WebContent/WEB-INF/applicationContext.xml")
// @TransactionConfiguration
// @Transactional
public class UserManagerTest
{
	@Autowired()
	UserManager userManager;

	@Test
	@Rollback(false)
	public void test()
	{
		// 创建一个角色。
		Role role = userManager.createRole("GUEST");
		Assert.assertTrue(role.getId() > 0);

		// 创建一个用户并把这个角色赋予这个用户。
		Account user = new Account();
		user.setUsername("userWithRole");
		user.setPassword("testpassword");

		// 准备要加入的角色
		Set<Long> roleIDs = new HashSet<Long>();
		roleIDs.add(role.getId());

		Account account = userManager.createAccount(user, roleIDs);
		Assert.assertTrue(account.getId() > 0);

		// 删除用户
		userManager.deleteAccount(account);
		user = userManager.queryAccountByName(account.getUsername());
		Assert.assertNull(user);
	}

	/**
	 * 用相同的用户名尝试创建几个用户。目标是不可以这样操作。
	 */
	@Test
	public void testCreateMutileAccount()
	{
		Account user = new Account();
		user.setUsername("userWithRole");
		user.setPassword("testpassword");
		//尝试创建两个用户
		userManager.createAccount(user);
		userManager.createAccount(user);
		//删除这个用户。
		userManager.deleteAccount(user);
		//看看还有没有这个用户名的用户
		user = userManager.queryAccountByName(user.getUsername());
		Assert.assertNull(user);
	}
	
	@Test
	public void testAddTestAccounts()
	{
		for (int i = 0; i < 100; i++)
		{
			Account userAccount = new Account();
			userAccount.setPassword("pass" + i);
			userAccount.setUsername("name" + i);

			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			session.save(userAccount);
			tx.commit();
		}
	}
}
