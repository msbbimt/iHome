package com.ihome.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.internal.NotNull;

/**
 * 用户帐号信息
 * 
 * @author Kinorsi Huang
 */

@Entity
@Table(name = "ACCOUNT",
		uniqueConstraints = { @UniqueConstraint(columnNames = { "USERNAME" }) })
@JsonAutoDetect(getterVisibility=Visibility.PROTECTED_AND_PUBLIC)
public class Account
{
	private long id;
	private String password;
	private String username;
	private Set<Role> roles;
	/**
	 * 用于乐观锁控制
	 */
	private Date lastUpdated;

	public Account()
	{
	}

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	@TableGenerator(
			name = "TABLE_GEN",
			table = "GENERATOR_TABLE",
//			schema = "IHOME",// 需要先在数据库中创建一个Schema
			pkColumnName = "ID_NAME",// ID的名称列
			valueColumnName = "ID_VALUE",// ID的值列
			pkColumnValue = "ACCOUNT_ID",// 本实体的ID名称
			allocationSize = 1)
	// 表示多增加多少条 记录数才会使ID_VALUE增加1.
	public long getId()
	{
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(long id)
	{
		this.id = id;
	}

	@NotNull
	@Column(name = "USERNAME")
	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	@NotNull
	@Column(name = "PASSWORD")
	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch=FetchType.EAGER)
	@JoinTable(
			name = "ACCOUNT_ROLE",
			joinColumns = @JoinColumn(name = "ACCOUNT_ID"),
			inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
	protected Set<Role> getRoles()
	{
		return roles;
	}

	protected void setRoles(Set<Role> roles)
	{
		this.roles = roles;
	}

	@JsonIgnore
	@Version
	@Column(name = "LAST_UPDATED")
	public Date getLastUpdated()
	{
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}

	public void addToRole(Role role)
	{
		this.getRoles().add(role);
		role.getAccounts().add(this);
	}

	public void removeFromRole(Role role)
	{
		this.getRoles().remove(role);
		role.getAccounts().remove(this);
	}
	
//	private void clearRole()
//	{
//		for(Role role : this.getRoles())
//		{
//			role.getAccounts().remove(this);
//		}
//		this.getRoles().clear();
//	}
//	
//	private void addRoles(Set<Role> roles)
//	{
//		for(Role role : roles)
//		{
//			addToRole(role);
//		}
//	}
//	
//	public void updateNewRoles(Set<Role> roles)
//	{
//		clearRole();
//		addRoles(roles);
//	}
}
