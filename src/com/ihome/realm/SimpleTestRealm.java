package com.ihome.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.ihome.dao.AccountDAO;

/**
 * 最简单的Shiro Realm，用来测试Realm自定义。
 */
public class SimpleTestRealm extends AuthorizingRealm
{
	@Autowired
	AccountDAO userAccountDAO;
	/**
	 * 权限
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection)
	{
		System.out.println("PrincipalCollection:" + principalCollection);
		
		
		return null;
	}

	/**
	 * 认证。官网说的步骤如下：
	 * 1)Inspects the token for the identifying principal (account identifying information)--从token取用户信息。
	 * 2)Based on the principal, looks up corresponding account data in the data source -- 到数据源中查找相关的用户信息。
	 * 3)Ensures that the token's supplied credentials matches those stored in the data store -- 比较数据源中取得的密码和token提供的是一样的。
	 * 4)If the credentials match, an AuthenticationInfo instance is returned that encapsulates the account data in a format Shiro understands
	 *   如果密码匹配，返回一个Shiro可以理解的AuthenticationInfo实例，实例中封装了用户信息。
	 * 5)If the credentials DO NOT match, an AuthenticationException is thrown--匹配密码失败则异常。
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException
	{
		UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;
		String username = token.getUsername();
		//从数据库中取用户信息(accountDAO.getAccount(username))，其中包含有密码。如：User user = userDao.getUser(username); if token.getPassword().equals(user.getPassword())
		userAccountDAO.create(null);
		//if(token.getPassword().equals(""))
		SimpleAuthenticationInfo authcInfo = new SimpleAuthenticationInfo(/*user.getName*/"kinorsi", /*user.getPassword*/"520luyasi", getName());
		return authcInfo;
	}
	
	@Override
	public boolean supports(AuthenticationToken token)
	{
		//说明本realm只是支持UsernamePasswordToken。这里只是测试吧，默认的实现似乎都是true的。
		return (token instanceof UsernamePasswordToken);
	}

}
