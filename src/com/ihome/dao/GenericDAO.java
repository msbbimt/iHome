package com.ihome.dao;

import java.util.List;

/**
 * 通用的CRUD
 * @author kinorsi
 *
 * @param <T> 实体
 */
public interface GenericDAO<T>
{
	T create(T entity);

	void delete(T entity);
	
	void deleteAll();
	
	int delete(List<Long> ids);

	T loadById(long id);

	void update(T entity);
	
	List<T> list();
	
	List<T> list(int start, int count);
	
	long getCount();
}
