package com.ihome.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ihome.entity.Account;
import com.ihome.entity.Permission;
import com.ihome.entity.Role;
import com.ihome.service.AccountManager;
import com.ihome.service.Authentication;
import com.ihome.service.PermissonManager;
import com.ihome.service.RoleManager;
import com.ihome.util.JsonResult;
import com.ihome.util.ResponseUtil;

@Controller
public class HomeController
{
	@Autowired
	Authentication auth;

	@Autowired
	AccountManager accountMgr;

	@Autowired
	PermissonManager perMgr;

	@Autowired
	RoleManager roleMgr;
	
	ObjectMapper objectMapper = new ObjectMapper();

	@RequestMapping(value = "/")
	public String home()
	{
		return "WEB-INF/views/home.jsp";
	}

	@RequestMapping(value = "manager")
	public String manager()
	{
		return "WEB-INF/views/manager/manager.html";
	}

	@RequestMapping(value = "/login")
	public String login()
	{
		auth.Authenticate("testuser", "testpassword");
		return "WEB-INF/views/home.jsp";
	}

	@RequestMapping(value = "/accounts2")
	public @ResponseBody
	List<Account> loadAllAccount2(@RequestHeader("Range") String range,
			HttpServletResponse res)
	{
		String[] ranges = range.split("=")[1].split("-");
		int start = Integer.parseInt(ranges[0]);
		int end = Integer.parseInt(ranges[1]);
		int count = end - start + 1;
		System.out.println(String.format("start=%s, end=%s", start, count));
		List<Account> accounts = accountMgr.loadAccounts(start, count);
		long total = accountMgr.getCount();

		res.setHeader("Content-Range", String.format("items=%s-%s/%s", start, end, total));
		return accounts;
	}

	@RequestMapping(value = "/accounts")
	public void loadAllAccount(@RequestHeader("Range") String range,
			HttpServletResponse res)
	{
		String[] ranges = range.split("=")[1].split("-");
		int start = Integer.parseInt(ranges[0]);
		int end = Integer.parseInt(ranges[1]);
		int count = end - start + 1;
		System.out.println(String.format("start=%s, end=%s", start, count));
		List<Account> accounts = accountMgr.loadAccounts(start, count);
		long total = accountMgr.getCount();

		res.setHeader("Content-Range", String.format("items=%s-%s/%s", start, end, total));

		try
		{
			String jsonAccounts = objectMapper.writeValueAsString(accounts);
			ResponseUtil.responseJSON(res, jsonAccounts);
		}
		catch (JsonProcessingException e)
		{
			// TODO 日志处理
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/updateRole")
	public @ResponseBody
	JsonResult updateRole(@RequestBody Map<String, Object> roleInfo)
	{
		Integer accountId = (Integer) roleInfo.get("account");
		List<Integer> newIds = (List<Integer>) roleInfo.get("newIds");
		List<Integer> delIds = (List<Integer>) roleInfo.get("deletedIds");

		List<Long> newIdsLong = new ArrayList<Long>();
		for (Integer newId : newIds)
		{
			newIdsLong.add(newId.longValue());
		}

		List<Long> delIdsLong = new ArrayList<Long>();
		for (Integer delId : delIds)
		{
			delIdsLong.add(delId.longValue());
		}

		accountMgr.delRolesToAccount(delIdsLong, accountId);
		accountMgr.addRolesToAccount(newIdsLong, accountId);

		JsonResult res = new JsonResult(true);

		return res;
	}

	@RequestMapping(value = "/delAccounts")
	public @ResponseBody
	String delAccounts(@RequestBody List<Long> ids)
	{
		System.out.println(ids);
		int count = accountMgr.deleteAccount(ids);
		return "delete ok " + count;
	}

	@RequestMapping(value = "/permissons")
	public @ResponseBody
	List<Permission> loadPermissionTree()
	{
		return perMgr.loadPermissionTree();
	}

	@RequestMapping("/roles")
	public @ResponseBody
	List<Role> loadAllRoles()
	{
		List<Role> roles = roleMgr.loadAll();
		return roles;
	}
}
