package com.ihome.service.impl;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import com.ihome.service.Authentication;

/**
 * 认证类
 * 
 * @author Kinorsi
 */
@Service
public class AuthenticationImpl implements Authentication
{
	public boolean Authenticate(String userName, String password)
	{
		Subject currentUser = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(userName, userName);
		token.setRememberMe(true);
		currentUser.login(token);
		return true;
	}
}
