package com.mes.loco.aps.server.service.po.andon;

import java.io.Serializable;
import java.util.Calendar;

/**
 * 台位信息
 */

public class AndonWorkspace implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AndonWorkspace() {
		// TODO Auto-generated constructor stub
	}

	public int ID;
	/**
	 * 名称
	 */
	public String Name = "";

	public String Code = "";

	public int Active = 0;
	/**
	 * 车型 产品类型 对应系统中productID
	 */
	public int ProductID = 0;
	/**
	 * 台位/库位 台位类型 1台位 2库位
	 */
	public int PlaceType = 0;

	public int ParentID = 0;

	public int CreatorID = 0;

	public String Creator = "";

	public Calendar CreateTime = Calendar.getInstance();

	public int EditorID = 0;

	public String Editor = "";

	public Calendar EditTime = Calendar.getInstance();

	public int PartID = 0;

	public String PartNo = "";

	public int OrderID = 0;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public int getPlaceType() {
		return PlaceType;
	}

	public void setPlaceType(int placeType) {
		PlaceType = placeType;
	}

	public int getCreatorID() {
		return CreatorID;
	}

	public void setCreatorID(int creatorID) {
		CreatorID = creatorID;
	}

	public String getCreator() {
		return Creator;
	}

	public void setCreator(String creator) {
		Creator = creator;
	}

	public Calendar getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(Calendar createTime) {
		CreateTime = createTime;
	}

	public int getEditorID() {
		return EditorID;
	}

	public void setEditorID(int editorID) {
		EditorID = editorID;
	}

	public String getEditor() {
		return Editor;
	}

	public void setEditor(String editor) {
		Editor = editor;
	}

	public Calendar getEditTime() {
		return EditTime;
	}

	public void setEditTime(Calendar editTime) {
		EditTime = editTime;
	}

	public int getPartID() {
		return PartID;
	}

	public void setPartID(int partID) {
		PartID = partID;
	}

	public String getPartNo() {
		return PartNo;
	}

	public void setPartNo(String partNo) {
		PartNo = partNo;
	}

	public int getProductID() {
		return ProductID;
	}

	public void setProductID(int productID) {
		ProductID = productID;
	}

	public int getActive() {
		return Active;
	}

	public void setActive(int active) {
		Active = active;
	}

	public int getParentID() {
		return ParentID;
	}

	public void setParentID(int parentID) {
		ParentID = parentID;
	}

	public String getCode() {
		return Code;
	}

	public void setCode(String code) {
		Code = code;
	}

	public int getOrderID() {
		return OrderID;
	}

	public void setOrderID(int orderID) {
		OrderID = orderID;
	}
}
