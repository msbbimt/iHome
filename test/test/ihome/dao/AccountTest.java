package test.ihome.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ihome.dao.AccountDAO;
import com.ihome.entity.Account;

/**
 * Account测试类。 Annotation的配置用于自动加载spring
 * 
 * @author kinorsi
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:WebContent/WEB-INF/applicationContext.xml")
//@TransactionConfiguration(transactionManager = "transactionManager", 
//	defaultRollback = false)
////有异常就回滚
//@Transactional(rollbackFor = Exception.class)
public class AccountTest
{
	@Autowired
	AccountDAO accountDAO ;//= new AccountDAOImpl();
	
//	@Autowired
//	SessionFactory sessionFactory;

	@Test
	public void testCreateAccount()
	{
		// 用户创建测试
		Account account = new Account();
		account.setUsername("testCreateAccount");
		account.setPassword("testCreateAccount");
		accountDAO.create(account);
		Assert.assertTrue(account.getId() > 0);
		
		//用户加载测试
		Account loadedAccount = accountDAO.loadById(account.getId());
		long id = loadedAccount.getId();
		String username = loadedAccount.getUsername();
		Assert.assertTrue(id > 0 && username != null);
	}
	
	@Test
	public void testDeleteAccount()
	{
		// 用户创建测试
		Account account = new Account();
		account.setUsername("testDeleteAccount");
		account.setPassword("testDeleteAccount");
		accountDAO.create(account);
		Assert.assertTrue(account.getId() > 0);
		
		//用户加载测试
		Account loadedAccount = accountDAO.loadById(account.getId());
		long id = loadedAccount.getId();
		String username = loadedAccount.getUsername();
		Assert.assertTrue(id > 0 && username != null);
		
		// 用户删除测试
		accountDAO.delete(account);
		loadedAccount = accountDAO.loadById(account.getId());
		Assert.assertNull(loadedAccount);
	}
	
	/**
	 * 批量插入测试数据
	 */
	@Test
	public void testCreateBatchAccount()
	{
		for (int i = 0; i < 100; i++)
		{
			Account account = new Account();
			account.setUsername("name" + i);
			account.setPassword("pass" + i);
			accountDAO.create(account);
		}
		
		Assert.assertTrue(true);
	}
	
	@Test 
	public void testClear()
	{
		accountDAO.deleteAll();
	}
}
