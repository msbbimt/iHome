package com.ihome.util;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public class ResponseUtil
{
	/**
	 * 以Json的格式返回结果
	 * @param res
	 * @param jsonStr
	 */
	public static void responseJSON(HttpServletResponse res, String jsonStr)
	{
		try
		{
			res.setContentType("text/json;characset=utf-8");
			res.getWriter().write(jsonStr);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
