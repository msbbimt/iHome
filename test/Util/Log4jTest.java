package Util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.ihome.controller.HomeController;

public class Log4jTest
{
	@Test
	public void test()
	{
		Logger logger = LogManager.getLogger(HomeController.class);
		logger.info("Hello, log4j2");
		logger.info(Log4jTest.class);
	}
}
