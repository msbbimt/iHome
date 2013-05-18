//custom/UserWidget.js
define([
    "dojo/_base/declare",
    "dijit/_WidgetBase",
    "dijit/_OnDijitClickMixin",
    "dijit/_TemplatedMixin",
    "dijit/_WidgetsInTemplateMixin",
    "dijit/Toolbar",
    "dijit/form/Button",
    "custom/HomeGrid",
    "dgrid/Selection",
    "dgrid/selector",
    "dojo/store/Memory",
    "dojo/store/JsonRest",
    "dijit/form/DropDownButton",
    "dijit/TooltipDialog",
    "dijit/layout/ContentPane",
    "dijit/layout/BorderContainer",
    "dijit/TitlePane",
    "dojo/request",
    "dojo/json",
    "dojo/_base/lang",
    "dojo/_base/array",
    "dojo/dom-construct",
    "dijit/form/CheckBox",
    "dojo/query",
    "dojox/form/Manager",
    "dojo/dom",
    "dojo/text!./UserWidget/template/UserWidget.html"
], function(declare, _WidgetBase,_OnDijitClickMixin, _TemplatedMixin,
            _WidgetsInTemplateMixin, Toolbar, Button, HomeGrid, Selection, selector,
            Memory, JsonRest, DropDownButton, TooltipDialog, ContentPane, BorderContainer, 
            TitlePane, request, json, lang, arrayUtil, domConstruct, CheckBox, query, Manager, 
            dom, template) {
 
    return declare("custom.UserWidget", [_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
        templateString: template,
        grid: null,
        
        /**
         * 初始化用户表格
         */
        _initGrid: function(){
        	//表格结构
			var columns = {
					select: selector({}),
					id: "ID",
					username: "User Name"
			};
			
		    //数据来源
		    var jsonStore = new JsonRest({target: "/iHome/accounts/", idProperty: "id"});
			this.grid = new HomeGrid({
				store: jsonStore,
				columns: {
						select: selector({}),
						id: "ID",
						username: "User Name"
				},
				selectionMode: "extended",
				allowSelectAll: true
			}, this._grid);
		    
	        this.grid.on("dgrid-select", lang.hitch(this, this._selectRows));
	        this.grid.on("dgrid-deselect", lang.hitch(this, this._deselectRows));
        },
        
        /**
         * 加载角色列表
         */
        _initRole: function(){
        	
        	var callback = lang.hitch(this, function(role){
        		var div = domConstruct.create("span");
        		var box = new CheckBox({id: this.id + "_role_" + role.id, disabled:true, name: this.id + "_role_"+role.id});
        		box.placeAt(div);
        		var label = domConstruct.create("label", {id: "roleLabel_" + role.id, innerHTML: " " + role.name, for: "role_" + role.id}, div);
        		domConstruct.place(div, this.role.containerNode);
        	});
        	
        	request.post("/iHome/roles", {handleAs: "json"}).then(function(roles){
        		arrayUtil.forEach(roles, callback);
        	});
        },
        
        /**
         * 设置选中相应的角色
         */
        _setRoles: function(roles){
        	var checkBoxs = query("input", this.role.containerNode);
        	console(checkBoxs);
        },
        
        /**
         * 修改角色
         */
        _editRole: function(){
        	var children = this.role.getChildren();
        	arrayUtil.forEach(children, function(child){
        		if(child.isInstanceOf(dijit.form.CheckBox)){
        			child.set("disabled", false);
        		}
        	});
        	this.editRole.set("disabled", true);
        	this.saveRole.set("disabled", false);
        	this.cancleRole.set("disabled", false);
        },
        
        _resetRole: function(){
        	var children = this.role.getChildren();
        	arrayUtil.forEach(children, function(child){
        		if(child.isInstanceOf(dijit.form.CheckBox)){
        			child.set("disabled", true);
        		}
        	});
        	this.editRole.set("disabled", false);
        	this.saveRole.set("disabled", true);
        	this.cancleRole.set("disabled", true);
        },
        
        _saveRole: function(){
        	this.editRole.set("disabled", false);
        	this.saveRole.set("disabled", true);
        	this.cancleRole.set("disabled", true);
        	
        	//取到所有的角色并找到选中的角色
        	var elements = this.role.getChildren();
        	var selectedRoles = arrayUtil.filter(elements, function(element){
        		return element.isInstanceOf(dijit.form.CheckBox) && element.get("checked");
        	});
        	var selectedRoleIds = arrayUtil.map(selectedRoles, function(selectedRole){
        		var lastPos = selectedRole.id.lastIndexOf("_");
        		return Number(selectedRole.id.substring(lastPos + 1));
        	});
        	
        	//选中的用户数
          	var selectedIds = new Array();
        	for(var i in this.grid.selection){
        		selectedIds.push(Number(i));
        	}
        	if(selectedIds.length != 1){
        		alert('选中的记录数不合适!');
        	}     
        	
        	var account = this.grid.row(selectedIds[0]).data;
        	var oldIds = arrayUtil.map(account.roles, function(role){
        		return role.id;
        	});
        	//当前选中的ID里在原有的ID里不存在，表示这是新加的。
        	var newIds = arrayUtil.filter(selectedRoleIds, function(selId){
        		for(var i in oldIds){
        			if(oldIds[i] == selId){
        				return false;
        			}
        		}
        		return true;
        	});
        	
        	//原有的ID，如果不在当前选中的ID里，表示这些是要删除的。
        	var delIds = arrayUtil.filter(oldIds, function(oldId){
        		for(var i in selectedRoleIds){
        			if(selectedRoleIds[i] == oldId){
        				return false;
        			}
        		}
        		return true;
        	});
        	
        	
        	//把当前的用户ID和ROLE ID提交过去。
        	var roleInfo = {
        		account: selectedIds[0],
        		newIds: newIds,
        		deletedIds: delIds
        	};
        	
        	//我顶，在使用promise的then之前构造好一个环境对象，一会就可以在里面访问我想要的东西了。
        	var env = {
        			grid: this.grid,
        			selectedId: selectedIds[0]
        	}
        	
        	request.post("/iHome/updateRole",{
        		headers: {
        			"Content-Type" : "application/json;charset=utf-8"
        		},
        		handleAs:"json",
        		data: json.stringify(roleInfo)
        	}).then(lang.hitch(env, function(res){
        		if(res.success){
        			this.grid.refresh({keepScrollPosition: true}).then(
        					lang.hitch(this, function(res){
        						this.grid.select(this.selectedId);
        					}));
        			console.log("");
        		}
        	}));
        },
        
        _cancleRole: function(){
        	this.editRole.set("disabled", false);
        	this.saveRole.set("disabled", true);
        	this.cancleRole.set("disabled", true);
        },
        
        /**
         * 删除选中的条目
         */
        _deleteSelected: function(event){
        	console.log(this.grid.selection); 
        	//构造ID数组
        	var ids = new Array();
        	for(var i in this.grid.selection){
        		ids.push(i);
        	}
        	
        	//如果不这样的话，取不到当前的对象。看看以后会不会有更简洁的方案再说呼。
        	var delCallback = lang.hitch(this, function(res){
        		this.grid.refresh();
        	});
        	
        	//请求删除
        	request.post("/iHome/delAccounts",{
        		headers: {
        			"Content-Type" : "application/json;charset=utf-8"
        		},
        		data: json.stringify(ids)
        	}).then(delCallback);
        },
        
        /**
         * 选中一行时 
         */
        _selectRows: function(event){ 
        	var grid = event.grid;
        	var roles = event.rows[0].data.roles;
        	var chboxes = this.role.getChildren();
        	arrayUtil.forEach(chboxes, function(ch){
        		ch.set("checked", false);
        	});
        	var env = {
        		grid: this
        	};
        	
        	//为了把grid的变量带到里面去。
        	var callback = lang.hitch(env, function(ch){
        		var env = {
        			grid: this.grid	
        		};
        		//再带一次
        		var callback = lang.hitch(env, function(role){
        			if(this.grid.id + "_role_"+ role.id == ch.id){
        				ch.set("checked", true);
        			}
        		});
        		
        		arrayUtil.forEach(roles, callback);
        	
        	});
        	arrayUtil.forEach(chboxes, callback);
        	this._resetRole();
        },
        
        _deselectRows: function(event){
        	var chboxes = this.role.getChildren();
        	arrayUtil.forEach(chboxes, function(ch){
        		ch.set("checked", false);
        	});
        },
        
        _show: function(){
        	alert('selected again');
        },
        
        _showSelected: function(){
        	alert('selected again');
        },
        
        postCreate: function(){
        	this.inherited(arguments);
        	this._initGrid();
        	this._initRole();
        },
        
        startup: function(){
        	this.inherited(arguments);
        	this.grid.startup();
        	//this.userPane.startup();
        },
        
        resize: function(){
        	this.inherited(arguments);
        	this.userPane.resize();
        	this.grid.resize();
        }
    });
 
});