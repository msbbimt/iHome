package com.ihome.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.internal.NotNull;

@Entity
@Table(name = "ROLE",
		// 保存角色的名字是唯一的。可是在hypersql下没有作用。需要在mysql里再测试一下了。真是纠结。
		uniqueConstraints = { @UniqueConstraint(name = "unique_name", columnNames = { "NAME" }) })
public class Role
{
	private long id;
	private String name;
	private Set<Permission> permissions;
	private Set<Account> accounts;
	private Date lastUpdated;

	public Role()
	{
	}

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	@TableGenerator(
			name = "TABLE_GEN",
			table = "GENERATOR_TABLE",
			// schema = "IHOME",// 需要先在数据库中创建一个Schema
			pkColumnName = "ID_NAME",// ID的名称列
			valueColumnName = "ID_VALUE",// ID的值列
			pkColumnValue = "ROLE_ID",// 本实体的ID名称
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
	@Column(name = "NAME")
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name.toUpperCase();
	}

	@JsonIgnore
	@ManyToMany(
			cascade = { CascadeType.PERSIST, CascadeType.MERGE },
			mappedBy = "roles")
	public Set<Account> getAccounts()
	{
		return accounts;
	}

	public void setAccounts(Set<Account> accounts)
	{
		this.accounts = accounts;
	}

	@JsonIgnore
	@ManyToMany(
			cascade = { CascadeType.PERSIST, CascadeType.MERGE },
			mappedBy = "roles")
	public Set<Permission> getPermissions()
	{
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions)
	{
		this.permissions = permissions;
	}

	@JsonIgnore
	@Version
	public Date getLastUpdated()
	{
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}

}
