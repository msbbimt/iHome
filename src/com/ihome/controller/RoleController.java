package com.ihome.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ihome.entity.Role;
import com.ihome.service.AccountManager;
import com.ihome.service.Authentication;
import com.ihome.service.PermissonManager;
import com.ihome.service.RoleManager;
import com.ihome.util.ResponseUtil;

@Controller
public class RoleController
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

	@RequestMapping(value = "/pageRoles")
	public void loadAllRoles(@RequestHeader("Range") String range,
			HttpServletResponse res)
	{
		String[] ranges = range.split("=")[1].split("-");
		int start = Integer.parseInt(ranges[0]);
		int end = Integer.parseInt(ranges[1]);
		int count = end - start + 1;
		System.out.println(String.format("start=%s, end=%s", start, count));
		List<Role> accounts = roleMgr.loadRoles(start, count);
		long total = roleMgr.getCount();

		res.setHeader("Content-Range", String.format("items=%s-%s/%s", start, end, total));

		try
		{
			String jsonAccounts = objectMapper.writeValueAsString(accounts);
			ResponseUtil.responseJSON(res, jsonAccounts);
		}
		catch (JsonProcessingException e)
		{
			e.printStackTrace();
		}
	}
}
