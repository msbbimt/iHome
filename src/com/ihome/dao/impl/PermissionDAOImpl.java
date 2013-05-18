package com.ihome.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.ihome.dao.PermissionDAO;
import com.ihome.entity.Permission;
import com.ihome.entity.Role;

@Repository
public class PermissionDAOImpl extends GenericHibernateDAO<Permission> implements PermissionDAO
{

	@Override
	public void addPermissonToRole(long permissionId, long roleId)
	{
		Session session = sessionFactory.getCurrentSession();
		Permission permission = (Permission) session.load(Permission.class, permissionId);
		Role role = (Role) session.load(Role.class, roleId);
		permission.addToRole(role);
	}

	@Override
	public void removePermissionFromRole(long permissionId, long roleId)
	{
		Session session = sessionFactory.getCurrentSession();
		Permission permission = (Permission) session.load(Permission.class, permissionId);
		Role role = (Role) session.load(Role.class, roleId);
		permission.removeFromRole(role);
	}

	@Override
	public void addSubToParent(long parentId, long subId)
	{
		Session session = sessionFactory.getCurrentSession();
		Permission parent = (Permission) session.load(Permission.class, parentId);
		Permission child = (Permission) session.load(Permission.class, subId);
		parent.addSubPermission(child);
	}

	/**
	 * 需要为覆盖进行级联删除
	 */
	@Override
	public int delete(List<Long> ids)
	{
		Session session = sessionFactory.getCurrentSession();
		int count = 0;
		for (long id : ids)
		{
			Permission per = (Permission)session.load(Permission.class, id);
			session.delete(per);
			count ++;
		}
		
		return count;
	}
	
	@Override
	public List<Permission> loadPermissionTree()
	{
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Permission.class);
		criteria.add(Restrictions.isNull("parent"));
		List<Permission> entities = (List<Permission>) criteria.list();
		
		return entities;
	}
}
