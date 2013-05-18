package com.ihome.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

/**
 * 返回结果用的。
 * 
 * @author kinorsi
 * 
 */
@JsonAutoDetect(getterVisibility = Visibility.PROTECTED_AND_PUBLIC)
public class JsonResult
{
	private boolean isSuccess;
	private String msg;

	public JsonResult(boolean isSuccess, String msg)
	{
		this.isSuccess = isSuccess;
		this.msg = msg;
	}

	public JsonResult(boolean isSuccess)
	{
		this.isSuccess = isSuccess;
	}

	public boolean isSuccess()
	{
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess)
	{
		this.isSuccess = isSuccess;
	}

	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

}
