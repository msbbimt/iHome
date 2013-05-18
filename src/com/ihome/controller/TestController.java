package com.ihome.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ihome.dao.AccountDAOImpl_without_spring;
import com.ihome.dao.AccountDAO;
import com.ihome.entity.Account;
import com.ihome.service.ITestService;

@Controller
public class TestController
{
	@Autowired
	ITestService testService;
	
	@Autowired
	AccountDAO accountDAO;
	
	@Autowired
	AccountDAOImpl_without_spring userAccountDAOImpl_without_spring;

	/**
	 * 测试Spring与SpringMVC的整合
	 * @return
	 */
	@RequestMapping(value = "/hello")
	public String test()
	{
		System.out.println(testService.helloService());
		return "WEB-INF/views/home.jsp";
	}
	
	public String getData()
	{
		return null;
	}
	
	@RequestMapping(value = "/testLogin")
	public String testLogin()
	{
		testService.loginTest();
		return null;
	}
	
	
	@RequestMapping(value = "/testAddUser")
	public String testAddUser()
	{
		Account user = new Account();
		user.setUsername("testuser");
		user.setPassword("testpassword");
		accountDAO.create(user);
		return "WEB-INF/views/home.jsp";
	}
	
	@RequestMapping(value = "/testLoadUser")
	public String testLoadUser()
	{
		Account a = accountDAO.loadById(12);
//		Account a = userAccountDAOImpl_without_spring.loadAccount(12);
		System.out.println(a.getUsername());
		return "WEB-INF/views/home.jsp";
	}
	
	@RequestMapping(value = "/testAddUser2")
	public String testAddUser2()
	{
		Account user = new Account();
		user.setUsername("testuser");
		user.setPassword("testpassword");
		userAccountDAOImpl_without_spring.addUserAccount(user);
		return "WEB-INF/views/home.jsp";
	}
}
