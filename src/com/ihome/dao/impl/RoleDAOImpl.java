package com.ihome.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.ihome.dao.RoleDAO;
import com.ihome.entity.Role;

@SuppressWarnings("unchecked")
@Repository
public class RoleDAOImpl extends GenericHibernateDAO<Role> implements RoleDAO
{

	@Override
	public Role loadByRoleName(String roleName)
	{
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Role.class);
		criteria.add(Restrictions.eq("name", roleName));
		List<Role> roles = (List<Role>)criteria.list();
		return roles.size() == 0 ? null : roles.get(0);
	}
//	@Autowired
//	private SessionFactory sessionFactory;
//	
	@Override
	public Role createByRoleName(String roleName)
	{
		Session session = sessionFactory.getCurrentSession();
		Role role = new Role();
		role.setName(roleName);
		session.save(role);
		return role;
	}
//
//	@Override
//	public Role loadRoleByName(String roleName)
//	{
//		Session session = sessionFactory.getCurrentSession();
//		Criteria criteria = session.createCriteria(Role.class);
//		criteria.add(Restrictions.eq("name", roleName));
//		List<Role> roles = (List<Role>)criteria.list();
//		return roles.size() == 0 ? null : roles.get(0);
//	}
//	
//	@Override
//	public void deleteRole(Role role)
//	{
//		Session session = sessionFactory.getCurrentSession();
//		session.delete(role);
//	}
//
//	@Override
//	public List<Role> loadAllRoles()
//	{
//		Session session = sessionFactory.getCurrentSession();
//		Criteria criteria = session.createCriteria(Role.class);
//		List<Role> roles = (List<Role>)criteria.list();
//		return roles;		
//	}
//	@Override
//	protected Class<Role> getGenericClass()
//	{
//		return Role.class;
//	}

}
