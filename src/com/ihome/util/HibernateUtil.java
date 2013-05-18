package com.ihome.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hibernate工具类
 * 
 * @author Kinorsi Huang
 * 
 */
public class HibernateUtil
{
	private static final SessionFactory sessionFactory = buildSessionFactory();

	private static Logger logger = LoggerFactory.getLogger(HibernateUtil.class);

	public static SessionFactory getSessionFactory()
	{
		return sessionFactory;
	}

	@SuppressWarnings("deprecation")
	private static SessionFactory buildSessionFactory()
	{
		try
		{
//			logger.info("开始创建SessionFactory");
			// Create the SessionFactory from hibernate.cfg.xml
			Configuration cfg = new Configuration();
			cfg.configure();
			ServiceRegistry serviceRegistry =new ServiceRegistryBuilder().applySettings(cfg.getProperties()).buildServiceRegistry();
			SessionFactory sessionFactory = cfg.configure().buildSessionFactory(serviceRegistry);
			return sessionFactory;
		}
		catch (Throwable ex)
		{
			// Make sure you log the exception, as it might be swallowed
//			logger.error("HibernateUtil初始化SessionFactory失败", ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

}
