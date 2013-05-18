package com.ihome.dao.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("unchecked")
public abstract class GenericHibernateDAO<T> implements com.ihome.dao.GenericDAO<T>
{
	private Class<T> entityClass;

	@Autowired
	protected SessionFactory sessionFactory;

	public GenericHibernateDAO()
	{
		/**
		 * 重要：本来getGenericSuperclass一次就可以取到T的真实类型的。但是当被代理包装之后，就不再那么好用。
		 * 这是反射相关的关系：Class<T> -> Type -> ParameterizedType。
		 * 如果getClass().getGenericSuperclass()为Class。说明这是个代理类。需要从这个代理类找到原始类，即此时的返回。
		 */
		Class clazz = getClass();
		Type genericClazz = getClass().getGenericSuperclass();
		if (genericClazz instanceof Class)
		{
			clazz = ((Class) genericClazz);
		}
		// 如果getSuperclass()等于getGenericSuperclass(),说明这是个代理类。
		entityClass = (Class<T>) ((ParameterizedType) clazz.getGenericSuperclass())
				.getActualTypeArguments()[0];
	}

	@Override
	public T create(T entity)
	{
		Session session = sessionFactory.getCurrentSession();
		session.save(entity);
		return entity;
	}

	@Override
	public void delete(T entity)
	{
		Session session = sessionFactory.getCurrentSession();
		session.delete(entity);
	}
	
	@Override
	public void deleteAll()
	{
		Session session = sessionFactory.getCurrentSession();
		String hqlDelete = String.format("delete %s", entityClass.getName());
		session.createQuery( hqlDelete )
			.executeUpdate();
	}

	@Override
	public T loadById(long id)
	{
		Session session = sessionFactory.getCurrentSession();
		T entity = (T) session.get(entityClass, id);
		return entity;
	}

	@Override
	public void update(T entity)
	{
		Session session = sessionFactory.getCurrentSession();
		session.update(entity);
	}

	@Override
	public List<T> list()
	{
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		List<T> entities = (List<T>) criteria.list();
		return entities;
	}
	
	@Override
	public List<T> list(int start, int count)
	{
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.setFirstResult(start);
		if(count > 0)
		{
			criteria.setMaxResults(count);
		}
		List<T> entities = (List<T>) criteria.list();
		return entities;
	}
	
	@Override
	public long getCount()
	{
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		criteria.setProjection(Projections.rowCount());
		List<Long> list = (List<Long>)criteria.list();
		return list.get(0);
	}
	
	@Override
	public int delete(List<Long> ids)
	{
		String clause = makeInClause(ids);
		
		Session session = sessionFactory.getCurrentSession();
		String hqlDelete = String.format("delete %s entity where entity.id in (%s)", entityClass.getName(), clause);
		int deletedEntities = session.createQuery( hqlDelete )
		        .executeUpdate();
		return deletedEntities;
	}
	
	

	/**
	 * 构造如(1, 2, 3)
	 * @param ids
	 * @return
	 */
	private String makeInClause(List<Long> ids)
	{
		StringBuilder sb = new StringBuilder();
		for(Long id : ids)
		{
			sb.append(id).append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
}
