/**
 * 定义用于此项目的grid。主要功能有：OnDemandGrid, Selection
 */
define([
    "dojo/_base/declare",
    "dgrid/OnDemandGrid",
    "dgrid/Selection",
    "dgrid/selector"
], function(declare, OnDemandGrid, Selection, selector){
		return declare("custom.HomeGrid", [OnDemandGrid, Selection],{
			selectionMode: "none",
			allowSelectAll: true
		});
});
