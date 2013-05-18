package com.ihome.entity;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.sun.istack.internal.NotNull;

//把约束给去掉了
//@Table(name = "PERMISSION",
//		uniqueConstraints = { @UniqueConstraint(name = "unique_permisson_name", columnNames = { "NAME", "parent_id" }) })
@Entity
@Table(name = "PERMISSION")
public class Permission
{
	private long id;
	private String name;
	private Set<Role> roles;
	private Permission parent;
	private Set<Permission> subPermissions;
	private Date lastUpdated;

	public Permission()
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
			pkColumnValue = "PERMISSION_ID",// 本实体的ID名称
			allocationSize = 1)
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
		this.name = name;
	}

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(
			name = "ROLE_PERMISSION",
			joinColumns = @JoinColumn(name = "PERMISSION_ID"),
			inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
	protected Set<Role> getRoles()
	{
		return roles;
	}

	protected void setRoles(Set<Role> roles)
	{
		this.roles = roles;
	}
	
	public void addToRole(Role role)
	{
		this.getRoles().add(role);
		role.getPermissions().add(this);
	}
	
	public void removeFromRole(Role role)
	{
		this.getRoles().remove(role);
		role.getPermissions().remove(this);
	}

	//JsonIngore用于保存不要出现父子、子父的循环引用 
	@JsonIgnore 
	@ManyToOne(cascade=CascadeType.ALL)
	public Permission getParent()
	{
		return parent;
	}

	public void setParent(Permission parent)
	{
		this.parent = parent;
	}

	@JsonProperty("children")
	@OneToMany(mappedBy="parent", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	public Set<Permission> getSubPermissions()
	{
		return subPermissions;
	}

	protected void setSubPermissions(Set<Permission> subPermissions)
	{
		this.subPermissions = subPermissions;
	}
	
	public void addSubPermission(Permission subPer)
	{
		this.getSubPermissions().add(subPer);
		subPer.setParent(this);
	}

	@Transient//表示不写入数据库
	public String getExpression()
	{
		return (this.parent == null) ? (this.name) : (this.parent.getName() + "." + this.name);
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
}
