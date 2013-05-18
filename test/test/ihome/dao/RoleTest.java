package test.ihome.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ihome.dao.AccountDAO;
import com.ihome.dao.PermissionDAO;
import com.ihome.dao.RoleDAO;
import com.ihome.entity.Account;
import com.ihome.entity.Permission;
import com.ihome.entity.Role;

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
public class RoleTest
{
	@Autowired
	RoleDAO roleDAO;
	
	@Autowired
	AccountDAO accountDAO;
	
	@Autowired
	PermissionDAO permissonDAO;
	
	@Test
	public void testCreateBatchRoles()
	{
		roleDAO.deleteAll();
		
		roleDAO.createByRoleName("Administrator");
		roleDAO.createByRoleName("Manager");
		roleDAO.createByRoleName("Operator");
		roleDAO.createByRoleName("Host");
		roleDAO.createByRoleName("Guest");
		
		List<Role> roles = roleDAO.list();
		Assert.assertTrue(roles.size() > 0);
	}
	
	@Test
	public void testClear()
	{
		roleDAO.deleteAll();
	}
	
	@Test
	public void testCreateRole()
	{
		String roleName = "Admin";
		Role role = roleDAO.createByRoleName(roleName);
		role = roleDAO.loadById(role.getId());
		Assert.assertTrue(role != null && role.getId() > 0);
		
		List<Role> roles = roleDAO.list();
		System.out.println("roles.size = " + roles.size());
	}
	
	@Test
	public void testSetRoleToAccount()
	{
		//创建一个role
		String roleName = "Role";
		Role role = roleDAO.loadByRoleName(roleName);
		if(role == null)
		{
			role = roleDAO.createByRoleName(roleName);
		}
		
		Permission p = new Permission();
		p.setName("ppp");
		Permission per = permissonDAO.create(p);
		
		permissonDAO.addPermissonToRole(per.getId(), role.getId());
		
		//创建一个account
		Account account = new Account();
		account.setUsername("testCreateAccount");
		account.setPassword("testCreateAccount");
		account = accountDAO.create(account);
		
		accountDAO.addRoleToAccount(role.getId(), account.getId());
		
		System.out.println("removePermissionFromRole");
		permissonDAO.removePermissionFromRole(per.getId(), role.getId());
		
		System.out.println("removeRoleFromAccount");
		accountDAO.removeRoleFromAccount(role.getId(), account.getId());

		
		System.out.println("");
	}
	
	@Test
	public void testCreateAccountWithRole()
	{
		//创建一个role
		String roleName = "Role";
		Role role = roleDAO.loadByRoleName(roleName);
		if(role == null)
		{
			role = roleDAO.createByRoleName(roleName);
		}
		
		Permission p = new Permission();
		p.setName("Permisson");
		Permission per = permissonDAO.create(p);
		
		permissonDAO.addPermissonToRole(per.getId(), role.getId());
		
		//创建一个account
		Account account = new Account();
		account.setUsername("testCreateAccount");
		account.setPassword("testCreateAccount");
		account = accountDAO.create(account);
		
		accountDAO.addRoleToAccount(role.getId(), account.getId());
	}
}
