package com.ihome.service.impl;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import com.ihome.service.ITestService;

/**
 * @Service(value="a") 这 时Autowire处就要加入@Quality("a")。表示根据名字进行查找 。
 */
@Service
public class TestService implements ITestService
{

	@Override
	public String helloService()
	{
		return "Hello Servie Test successfully!";
	}

	@Override
	public String loginTest()
	{
		Subject currentUser = SecurityUtils.getSubject();
	    UsernamePasswordToken token = new UsernamePasswordToken("lonestarr", "vespa");

	    token.setRememberMe(true);
	    currentUser.login(token);
		return null;
	}
	
	

}
