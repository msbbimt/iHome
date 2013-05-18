package test.ihome.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ihome.dao.PermissionDAO;
import com.ihome.entity.Permission;

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
public class PermissionTest
{
	@Autowired
	PermissionDAO perDAO ;//= new AccountDAOImpl();
	
	@Test
	public void buildTree()
	{
		//TODO 记得测试相同的子菜单的问题
		/**
		 * tree 像这样
		 * 用户权限
		 *    |-增加用户
		 *    |-删除用户
		 * 菜单权限
		 * 	  |-增加菜单
		 * 	  |-删除菜单
		 * 测试权限
		 *    |-增加权限
		 *        |-增加个人权限
		 *        |-删除个人权限
		 * 
		 */
		//清空
		perDAO.deleteAll();
		
		//用户
		Permission userRoot = new Permission();
		userRoot.setName("用户权限");
		perDAO.create(userRoot);
		
		Permission userAdd = new Permission();
		userAdd.setName("增加");
		perDAO.create(userAdd);
		
		Permission userDel = new Permission();
		userDel.setName("删除");
		perDAO.create(userDel);
		
		perDAO.addSubToParent(userRoot.getId(), userAdd.getId());
		perDAO.addSubToParent(userRoot.getId(), userDel.getId());
		
		//菜单 
		Permission menuRoot = new Permission();
		menuRoot.setName("菜单权限");
		perDAO.create(menuRoot);
		
		Permission menuAdd = new Permission();
		menuAdd.setName("增加");
		perDAO.create(menuAdd);
		
		Permission menuDel = new Permission();
		menuDel.setName("删除");
		perDAO.create(menuDel);
		
		perDAO.addSubToParent(menuRoot.getId(), menuAdd.getId());
		perDAO.addSubToParent(menuRoot.getId(), menuDel.getId());
		
		//测试权限
		Permission testRoot = new Permission();
		testRoot.setName("测试权限");
		perDAO.create(testRoot);
		
		Permission testAdd = new Permission();
		testAdd.setName("增加权限");
		perDAO.create(testAdd);
		
		perDAO.addSubToParent(testRoot.getId(), testAdd.getId());
		
		Permission testPerAdd = new Permission();
		testPerAdd.setName("增加个人权限");
		perDAO.create(testPerAdd);
		
		Permission testPerDel = new Permission();
		testPerDel.setName("删除个人权限");
		perDAO.create(testPerDel);
		
		perDAO.addSubToParent(testAdd.getId(), testPerAdd.getId());
		perDAO.addSubToParent(testAdd.getId(), testPerDel.getId());
	}
	
	@Test
	public void loadTree()
	{
		Permission per = perDAO.loadById(13);
		Set<Permission> subs = per.getSubPermissions();
		Assert.assertTrue(per.getSubPermissions().size() > 0);
		System.out.println(per.getExpression());
		for (Iterator iterator = subs.iterator(); iterator.hasNext();)
		{
			Permission permission = (Permission) iterator.next();
			System.out.println(permission.getExpression());
		}
	}
	
	@Test
	public void loadPerTree()
	{
		List<Permission> tree = perDAO.loadPermissionTree();
		System.out.println(tree);
	}
	
	@Test
	public void delParent()
	{
		List<Long> ids = new ArrayList<Long>();
		ids.add(19L);
		perDAO.delete(ids);
	}
}
